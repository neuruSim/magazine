package com.shop.magazine.domain.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.magazine.domain.user.dto.UserResponseDto;
import com.shop.magazine.domain.user.dto.UserSaveRequestDto;
import com.shop.magazine.domain.user.dto.UsersResponseDto;
import com.shop.magazine.domain.user.entity.User;
import com.shop.magazine.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserApiController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @DisplayName("GET_user_API_ALL")
    @Test
    void findList() throws Exception {
        // given
        User user = User.builder()
                .email("test@naver.com")
                .username("testId")
                .password("1234")
                .phone("01022223333")
                .build();

        List<User> userList = new ArrayList<>();
        userList.add(user);

        given(userService.findAll()).willReturn(UsersResponseDto.of(userList));

        // when
        ResultActions actions = mvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userResponseList[0].username", is("testId")))
                .andDo(print());

    }

    @DisplayName("GET_user_API_1")
    @Test
    void findByUser() throws Exception {

        // given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .username("testId")
                .password("1234")
                .phone("01022223333")
                .build();

        UserResponseDto response = new UserResponseDto(user);

        given(userService.findById(user.getId())).willReturn(response);

        // when
        ResultActions actions = mvc.perform(get("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("testId")))
                .andDo(print());
    }

    @DisplayName("POST user API")
    @Test
    void register() throws Exception {
        // given
        UserSaveRequestDto request = UserSaveRequestDto.builder()
                .email("test@naver.com")
                .name("testId")
                .password("12345")
                .phone("01022223333")
                .build();


        given(userService.register(request)).willReturn(1L);

        // when
        ResultActions actions = mvc.perform(post("/api/v1/user")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    @DisplayName("DELETE user API")
    @Test
    void remove() throws Exception {

        //given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .username("testId")
                .password("1234")
                .phone("01022223333")
                .build();

        willDoNothing().given(userService).remove(user.getId());

        // when
        ResultActions actions = mvc.perform(delete("/api/v1/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andDo(print());

    }

}