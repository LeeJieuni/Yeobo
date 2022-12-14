package com.jagi.yeobo.controller;

import com.jagi.yeobo.domain.Bag;
import com.jagi.yeobo.dto.*;
import com.jagi.yeobo.service.BagService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;
//@CrossOrigin(originPatterns = "https://localhost:3000, http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class BagController {

    private final BagService bagService;

    @ApiOperation(value = "보따리 이름과 메모를 수정한다.",notes = "userId에 해당하는 회원의 보따리의 이름과 메모를 수정한다.")
    @PutMapping("api/bag/update/name/{bagId}")
    public ResponseEntity<?> updateBag(@PathVariable("bagId") long bagId, @RequestBody BagDto bagDto){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        bagService.updateBag(bagId, bagDto);
        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 수정 성공");

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리 리스트를 조회한다.",notes = "userId에 해당하는 회원의 보따리 리스트를 조회한다.")
    @GetMapping("api/bag/list/{userId}")
    public ResponseEntity<?> searchBagList(@PathVariable("userId") long userId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<BagDto> bagDtoList = bagService.searchBagList(userId);

        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 리스트 조회 성공");
        message.setData(bagDtoList);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리에 좋아요를 누른다.",notes = "좋아요를 누른 보따리에 좋아요 수를 증가시킨다.")
    @PostMapping("api/bag/like/{userId}/{bagId}")
    public ResponseEntity<?> likeBag(@PathVariable("userId") long userId, @PathVariable("bagId") int bagId)  {
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            bagService.likeBag(userId, bagId);

            message.setStatus(StatusEnum.OK);
            message.setMessage("좋아요 성공");
            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }catch(DuplicateKeyException e) {
            e.printStackTrace();
            message.setStatus(StatusEnum.INTERNAL_SERVER_ERROR);
            message.setMessage("좋아요 중복 발생");
            return new ResponseEntity<>(message, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @ApiOperation(value = "좋아요한 보따리의 리스트를 조회한다.",notes = "userId에 해당하는 회원이 좋아요를 누른 보따리의 리스트를 출력한다.")
    @GetMapping("api/bag/likelist/{userId}")
    public ResponseEntity<?> searchLikeBagList(@PathVariable("userId") long userId){
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<BagDto> bagDtoList = bagService.searchLikeBagList(userId);
        message.setStatus(StatusEnum.OK);
        message.setMessage("좋아요한 보따리 리스트 조회 성공");
        message.setData(bagDtoList);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "인기 보따리의 리스트를 조회한다.",notes = "상위 4개 좋아요 순의 인기 보따리들의 리스트를 출력한다.")
    @GetMapping("api/temp/bag/list/popular")
    public ResponseEntity<?> searchPopularBagList(){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<PopularBagDto> bagDtoList = bagService.searchPopularBagList();
        message.setStatus(StatusEnum.OK);
        message.setMessage("상위 4개 인기 보따리 리스트 조회 성공");
        message.setData(bagDtoList);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리 상세보기를 한다.",notes = "해당 보따리의 이름, 메모, 보따리에 담겨있는 여행지 리스트를 출력한다.")
    @GetMapping("api/temp/bag/detail/{bagId}/{userId}")
    public ResponseEntity<?> searchDetailBag(@PathVariable("bagId") long bagId, @PathVariable("userId") long userId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        BagDetailDto bagDetailDto = bagService.searchDetailBag(bagId,userId);
        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 상세보기 성공");
        message.setData(bagDetailDto);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리를 검색 조회한다.",notes = "보따리의 이름을 입력하여 보따리를 검색한다.")
    @GetMapping("api/temp/bag/{name}/{userId}")
    public ResponseEntity<?> searchBagByName(@PathVariable("name") String name, @PathVariable("userId") long userId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<BagSearchDto> bagDtoList = bagService.searchBagByName(name, userId);
        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 이름 조회 성공");
        message.setData(bagDtoList);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리에서 여행지를 수정한다.",notes = "보따리에서 해당 하는 여행지를 수정(삭제)한다.")
    @DeleteMapping("api/bag/update/{bagId}/{attractionId}")
    public ResponseEntity<?> deleteOneInBag(@PathVariable("bagId") long bagId, @PathVariable("attractionId") long attractionId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        int result = bagService.deleteOneInBag(bagId, attractionId);
        if(result==0){
            message.setMessage("잘못된 요청. bagId나 attractionId가 존재하는 값인지 잘 맞는지 확인");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 여행지 한개 삭제 성공");
        return new ResponseEntity<>(message, headers, HttpStatus.OK);

    }

    @ApiOperation(value = "좋아요한 보따리를 취소한다.",notes = "해당 보따리의 좋아요를 취소한다.")
    @DeleteMapping("api/bag/delete/like/{userId}/{bagId}")
    public ResponseEntity<?> likeBagCancel(@PathVariable("userId") long userId, @PathVariable("bagId") long bagId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        int result = bagService.likeBagCancel(userId, bagId);
        if(result==0){
            message.setMessage("잘못된 요청. bagId나 userId가 존재하는 값인지 잘 맞는지 확인");
            return new ResponseEntity<>(message, headers, HttpStatus.BAD_REQUEST);
        }
        message.setStatus(StatusEnum.OK);
        message.setMessage("좋아요 취소 성공");
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리를 생성한다.",notes = "user id와 여행지 리스트, 보따리 이름,메모를 사용하여 보따리를 생성한다.")
    @PostMapping("api/bag/create/{userId}")
    public ResponseEntity<?> createBag(@PathVariable("userId") long userId, @RequestBody BagResponseDto bagResponseDto){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Bag bag = bagService.createBag(userId, bagResponseDto);
        bagService.createAttractions(bag.getId(), bagResponseDto);

        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리 생성 성공");
        message.setData("bagId:"+ bag.getId());
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "보따리에 여행지를 담는다.",notes = "보따리에 여행지 한개를 담는다. (수정)")
    @PostMapping("api/bag/{bagId}/{attractionId}")
    public ResponseEntity<?> createOneAttInBag(@PathVariable("bagId") long bagId, @PathVariable("attractionId") long attractionId){
        Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        bagService.createOneAttInBag(bagId, attractionId);

        message.setStatus(StatusEnum.OK);
        message.setMessage("보따리에 여행지 담기 성공");
        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

}
