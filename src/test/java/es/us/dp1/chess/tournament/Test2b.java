package es.us.dp1.chess.tournament;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class Test2b {
    @Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

    private String url = "/api/v1/matches";
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
		.webAppContextSetup(context)
		.apply(SecurityMockMvcConfigurers.springSecurity())
		.build();
	}



    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void test2bCanAllMatches() throws JsonProcessingException, Exception{
        mockMvc.perform(get(url))
			.andExpect(status().isOk())            
            .andExpect(jsonPath("$", hasSize(3)));
    }    

    @Test
    @Transactional
    @WithMockUser(username = "player2", authorities = {"PLAYER"})
    public void test2bCanGetASpecificMatch() throws JsonProcessingException, Exception{
        mockMvc.perform(get(url+"/5"))
			.andExpect(status().isOk())            
            .andExpect(jsonPath("$.whitePlayer.username", is("player1")))
            .andExpect(jsonPath("$.name", is("Partida 5")));
    }    

    

    @Test
    @Transactional
    @WithMockUser(username = "player1", authorities = {"PLAYER"})
    public void test2bCannotGetNonExistentMatch() throws JsonProcessingException, Exception{
       mockMvc.perform(get(url+"/474629"))
			.andExpect(status().isNotFound());
    }

    
    @Test
    @Transactional    
    public void test2bCannotGetMatchIfYouAreNotPlayer() throws JsonProcessingException, Exception{
       // Perform request as ADMIN
        mockMvc.perform(get(url + "/1")
            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
            .andExpect(status().isForbidden());

        // Perform request as PLAYER
        mockMvc.perform(get(url + "/5")
            .with(user("player1").authorities(new SimpleGrantedAuthority("PLAYER"))))
            .andExpect(status().isOk());
    }
    
}
