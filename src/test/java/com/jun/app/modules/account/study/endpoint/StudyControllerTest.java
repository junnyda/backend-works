package com.jun.app.modules.account.study.endpoint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jun.app.WithAccount;
import com.jun.app.account.infra.repository.AccountRepository;
import com.jun.app.modules.account.domain.entity.Account;
import com.jun.app.modules.study.application.StudyService;
import com.jun.app.modules.study.endpoint.form.StudyForm;
import com.jun.app.modules.study.infra.repository.StudyRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StudyControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired StudyRepository studyRepository;
    @Autowired StudyService studyService;

    @Test
    @DisplayName("스터디 폼 조회")
    @WithAccount("jaime")
    void studyForm() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @Test
    @DisplayName("스터디 추가: 정상")
    @WithAccount("jaime")
    void createStudy() throws Exception {
        String studyPath = "study-test";
        mockMvc.perform(post("/new-study")
                        .param("path", studyPath)
                        .param("title", "study-title")
                        .param("shortDescription", "short-description")
                        .param("fullDescription", "fullDescription")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath));
        assertTrue(studyRepository.existsByPath(studyPath));
    }

    @Test
    @DisplayName("스터디 추가: 입력값 비정상")
    @WithAccount("jaime")
    void createStudyWithError() throws Exception {
        String studyPath = "s";
        mockMvc.perform(post("/new-study")
                        .param("path", studyPath)
                        .param("title", "study-title")
                        .param("shortDescription", "short-description")
                        .param("fullDescription", "fullDescription")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("스터디 추가: 입력값 중복")
    @WithAccount("jaime")
    void createStudyWithDuplicate() throws Exception {
        Account account = accountRepository.findByNickname("jaime");
        String duplicatedPath = "study-path";
        studyService.createNewStudy(StudyForm.builder()
                .path(duplicatedPath)
                .title("study-title")
                .shortDescription("short-description")
                .fullDescription("full-description")
                .build(), account);
        mockMvc.perform(post("/new-study")
                        .param("path", duplicatedPath)
                        .param("title", "study-title")
                        .param("shortDescription", "short-description")
                        .param("fullDescription", "fullDescription")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors());
    }
}