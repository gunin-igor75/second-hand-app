package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdsDTO;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.FullAds;
import ru.skypro.homework.dto.ResponseWrapperAds;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Photo;
import ru.skypro.homework.entity.Picture;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.PhotoService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.FileManager;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {
    private final AdsRepository adsRepository;
    private final FileManager fileManager;
    private final PhotoService photoService;
    private final UserService userService;
    private final AdsMapper mapper;

    @Value("${ads.picture.dir.path}")
    private String DIRECTORY_PICTURE;

    @Value("${image.endpoint}")
    private String ENDPOINT_IMAGE;

    @Override
    public ResponseWrapperAds getAllAds() {
        List<AdsDTO> ads = adsRepository.findAll().stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(ads.size())
                .results(ads)
                .build();
    }

    @Override
    @Transactional
    public AdsDTO createAds(CreateAds createAds, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, DIRECTORY_PICTURE);
        fileManager.upLoadFile(file, filePath);
        Ads ads = mapper.createAdsToAds(createAds);
        Ads persistentAds = adsRepository.save(ads);
        Photo picture = createPicture(persistentAds, file, filePath);
        persistentAds.setImage(ENDPOINT_IMAGE + picture.getId());
        return mapper.adsToAdsDTO(adsRepository.save(persistentAds));
    }

    private Photo createPicture(Ads ads, MultipartFile file, Path path) {
        Picture picture = new Picture();
        picture.setAds(ads);
        picture.setFilePath(path.toString());
        picture.setFileSize(file.getSize());
        picture.setMediaType(file.getContentType());
        return photoService.createPhoto(picture);
    }

    @Override
    public FullAds getAds(int id) {
        Ads ads = adsRepository.getReferenceById(id);
        return mapper.adsToFullAds(ads);
    }

    @Override
    public void deleteAds(int id) {
        Ads ads = adsRepository.getReferenceById(id);
        adsRepository.delete(ads);
    }

    @Override
    public AdsDTO updateAds(int id, CreateAds createAds) {
        Ads ads = adsRepository.getReferenceById(id);
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        ads.setTitle(createAds.getTitle());
        return mapper.adsToAdsDTO(ads);
    }

    @Override
    public ResponseWrapperAds getAdsMe() {
        Users authorizedUser = userService.getAuthorizedUser();
        Integer id = authorizedUser.getId();
        List<AdsDTO> adsMe = adsRepository.findAdsByUserId(id)
                .stream()
                .map(mapper::adsToAdsDTO)
                .collect(Collectors.toList());
        return ResponseWrapperAds.builder()
                .count(adsMe.size())
                .results(adsMe)
                .build();
    }

    @Override
    public String updatePictureByAds(int id, MultipartFile file) {
        Path filePath = fileManager.getRandomPath(file, DIRECTORY_PICTURE);
        fileManager.upLoadFile(file, filePath);
        Photo picture = photoService.getCurrentPicture(id);
        fileManager.checkExistFileAndDelete(picture.getFilePath());
        picture.setFilePath(filePath.toString());
        photoService.createPhoto(picture);
        Ads ads = adsRepository.getReferenceById(id);
        return ads.getImage();
    }
}
