//package de.iav.backend.controller;
//
//import de.iav.backend.model.TimeSlot;
//import de.iav.backend.repository.TimeSlotRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class TimeSlotControllerIntegrationOldTest {
//
//    private static final String BACKEND_TIMESLOTS_URL = "/api/toastMasterManager/timeslots";
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TimeSlotRepository timeSlotRepository;
//
//    @Test
//    @DirtiesContext
//    void getAllStudents_whenApiCalledAndListIsEmpty_thenExpectStatusOkAndReturnEmptyListAsJson() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get(BACKEND_TIMESLOTS_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json("[]"));
//    }
//
//    @Test
//    @DirtiesContext
//    void getAllStudents_whenApiCalledAndListIsNotEmpty_thenExpectStatusOkAndReturnListOfStudentsAsJson() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(BACKEND_TIMESLOTS_URL)
//                        .contentType("application/json")
//                        .content(
//                                """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                        """
//                        ))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                 """
//                ));
//
//        mockMvc.perform(MockMvcRequestBuilders.get(BACKEND_TIMESLOTS_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                [
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                ]
//                                """
//                ));
//    }
//
//    @Test
//    @DirtiesContext
//    void getStudentById_whenStudentByIdExist_thenExpectStatusOkAndReturnStudent() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(BACKEND_TIMESLOTS_URL)
//                        .contentType("application/json")
//                        .content(
//                                """
//                                         {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                       """
//                        ))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                               """
//                ));
//
//        mockMvc.perform(MockMvcRequestBuilders.get(BACKEND_TIMESLOTS_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                """
//                ));
//    }
//
//    @Test
//    @DirtiesContext
//    void addStudent_whenApiCalled_thenExpectStatusOkAndReturnSavedStudent() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(BACKEND_TIMESLOTS_URL)
//                        .contentType("application/json")
//                        .content(
//                                """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                        """
//                        ))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                """
//                ));
//    }
//
//    @Test
//    @DirtiesContext
//    void updateStudent_whenStudentExist_thenExpectStatusOkAndReturnUpdatedStudent() throws Exception {
//        TimeSlot student = new TimeSlot("1", "Mathias", "Testrede", "1:00","1:30","2:00");
//        timeSlotRepository.save(student);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(BACKEND_TIMESLOTS_URL)
//                        .contentType("application/json")
//                        .content(
//                                """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                        """
//                        ))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                """
//                ));
//    }
//
//    @Test
//    @DirtiesContext
//    void deleteStudent_whenStudentExist_thenExpectStatusOk() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post(BACKEND_TIMESLOTS_URL)
//                        .contentType("application/json")
//                        .content(
//                                """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                                        """
//                        ))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json(
//                        """
//                                        {
//                                            "id": "1",
//                                            "title": "Mathias",
//                                            "description": "Testrede",
//                                            "green": "1:00",
//                                            "amber": "1:30",
//                                            "red": "2:00"
//                                        }
//                        """
//                ));
//
//        mockMvc.perform(MockMvcRequestBuilders.delete(BACKEND_TIMESLOTS_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
///*
//    @Test
//    void getAllTimeSlots() {
//    }
//
//    @Test
//    void getTimeSlotById() {
//    }
//
//    @Test
//    void setDefaultTimeSlots() {
//    }
//
//    @Test
//    void createTimeSlot() {
//    }
//
//    @Test
//    void updateTimeSlot() {
//    }
//
//    @Test
//    void deleteTimeSlot() {
//    }*/
//}
