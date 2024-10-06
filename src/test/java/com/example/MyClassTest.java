import org.junit.jupiter.api.Test;
 
class MyClassTest {
 
     @Test
     void testGenerateCsrfToken() {
         HttpSession session = mock(HttpSession.class);
         String token = CSRFGen.generateCsrfToken(session);
         assertNotNull(token);
         verify(session).setAttribute("csrfToken", token);
     }
     
     @Test
     @WithMockUser(username = "admin", roles = {"ADMIN"})
     void testAdminAccess() throws Exception {
         mockMvc.perform(get("/admin"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("admin"));
     }
     
     @Test
     @WithMockUser(username = "guest", roles = {"GUEST"})
     void testAdminAccessDeniedForGuest() throws Exception {
         mockMvc.perform(get("/admin"))
                 .andExpect(status().isForbidden());
     }
}
