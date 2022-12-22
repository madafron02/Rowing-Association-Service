package nl.tudelft.sem.template.users.controllers;

import nl.tudelft.sem.template.users.authentication.AuthManager;
import nl.tudelft.sem.template.users.domain.User;
import nl.tudelft.sem.template.users.domain.UserService;
import nl.tudelft.sem.template.users.domain.database.OrganisationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;



public class UserControllerTest {

    private transient UserController userController;
    private transient AuthManager authManager;
    private transient UserService userService;
    private transient OrganisationRepo organisationRepo;


    private transient MockMvc mockMvc;

    /**
     * Sets up mocks and controller required for testing.
     */
    @BeforeEach
    public void setup() {
        authManager = Mockito.mock(AuthManager.class);
        userService = Mockito.mock(UserService.class);
        organisationRepo = Mockito.mock(OrganisationRepo.class);

        userController = new UserController(authManager, userService, organisationRepo);

        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void constructorTest() {
        assertThat(userController).isNotNull();
    }

    @Test
    public void getUserDataTest() throws Exception {
        User testUser = new User("abc@def.ghi", null, false, null, null);
        when(authManager.getNetId()).thenReturn("abc@def.ghi");
        when(userService.getByEmail("abc@def.ghi")).thenReturn(testUser);

        this.mockMvc.perform(get("/user/mydata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@def.ghi"));

    }

    @Test
    public void getEmptyUserDataTest() throws Exception  {
        when(authManager.getNetId()).thenReturn("test@a.b");
        when(userService.getByEmail("test@a.b")).thenReturn(null);

        this.mockMvc.perform(get("/user/mydata"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with the email: test@a.b was not found."));
    }

    @Test
    public void getDetailsTest() throws Exception {
        User testUser = new User("abc@def.ghi", null, false, null, null);
        when(userService.getByEmail("abc@def.ghi")).thenReturn(testUser);

        this.mockMvc.perform(post("/user/details")
                .content("abc@def.ghi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abc@def.ghi"));
    }

    @Test
    public void getEmptyDetailsTest() throws Exception {
        when(userService.getByEmail("abc@def.ghi")).thenReturn(null);

        this.mockMvc.perform(post("/user/details")
                        .content("abc@def.ghi"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with the email: abc@def.ghi was not found."));
    }

    @Test
    public void createAccountNotAuthTest() throws Exception {
        when(authManager.getNetId()).thenReturn("notTheSameEmail@invalid.com");

        this.mockMvc.perform(post("/user/newuser")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"email\":\"test@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("You are not authenticated with the email: test@a.b"));
    }

    @Test
    public void createDuplicateAccountTest() throws Exception {
        when(authManager.getNetId()).thenReturn("test@a.b");
        when(userService.userExists("test@a.b")).thenReturn(true);

        this.mockMvc.perform(post("/user/newuser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"test@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("The following e-mail is already in use: test@a.b"));
    }

    @Test
    public void createNewAccountTest() throws Exception {
        when(authManager.getNetId()).thenReturn("test@a.b");
        when(userService.userExists("test@a.b")).thenReturn(false);

        User testUser = new User("test@a.b", null, false, null, null);
        when(userService.saveUser(testUser)).thenReturn(testUser);

        this.mockMvc.perform(post("/user/newuser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"test@a.b\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@a.b"));
    }

    @Test
    public void createInvalidAccountTest() throws Exception {
        when(authManager.getNetId()).thenReturn("test2@a.b");
        when(userService.userExists("test2@a.b")).thenReturn(false);

        User testUser = new User("test2@a.b", null, false, null, null);
        when(userService.saveUser(testUser)).thenThrow(new IllegalArgumentException("Test Exception"));

        this.mockMvc.perform(post("/user/newuser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"test2@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Test Exception"));
    }

    @Test
    public void updateNotAuthTest() throws Exception {
        when(authManager.getNetId()).thenReturn("differentEmail@a.b");

        this.mockMvc.perform(post("/user/updatemydata")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"testemail@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("You are not authenticated with the email: testemail@a.b"));
    }

    @Test
    public void updateNonExistentTest() throws Exception {
        when(authManager.getNetId()).thenReturn("testemail@a.b");
        when(userService.userExists("testemail@a.b")).thenReturn(false);

        this.mockMvc.perform(post("/user/updatemydata")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"testemail@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("This user does not exist, please create a user account first."));
    }

    @Test
    public void updateSuccessfulTest() throws Exception {
        when(authManager.getNetId()).thenReturn("testemail@a.b");
        when(userService.userExists("testemail@a.b")).thenReturn(true);
        User testUser = new User("testemail@a.b", null, false, null, null);
        when(userService.updateUser(testUser)).thenReturn(testUser);

        this.mockMvc.perform(post("/user/updatemydata")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"testemail@a.b\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testemail@a.b"));
    }

    @Test
    public void updateWithInvalidDetailsTest() throws Exception {
        when(authManager.getNetId()).thenReturn("testemail@a.b");
        when(userService.userExists("testemail@a.b")).thenReturn(true);
        User testUser = new User("testemail@a.b", null, false, null, null);
        when(userService.updateUser(testUser)).thenThrow(new IllegalArgumentException("test exception"));

        this.mockMvc.perform(post("/user/updatemydata")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"email\":\"testemail@a.b\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("test exception"));
    }

    @Test
    public void addDuplicateOrganisationTest() throws Exception {
        when(organisationRepo.existsOrganisationByName("testOrganisation")).thenReturn(true);

        this.mockMvc.perform(post("/user/organisation/add")
                        .content("testOrganisation"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Organisation already present in system."));
    }

    @Test
    public void addOrganisationTest() throws Exception {
        when(organisationRepo.existsOrganisationByName("testOrganisation")).thenReturn(false);

        this.mockMvc.perform(post("/user/organisation/add")
                        .content("testOrganisation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testOrganisation"));

        verify(organisationRepo, times(1)).save(any());
    }

}
