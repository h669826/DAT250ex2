package com.example.demo;

import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DomainScenarioTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired com.fasterxml.jackson.databind.ObjectMapper om;

    @BeforeEach
    void addJackson() {
        rest.getRestTemplate().getMessageConverters()
                .add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(om));
    }

    private String baseUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    void fullScenario() {
        String json =
                """
                        {"username":"Donald","email":"123@123"}
        """;
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Void> r = rest.postForEntity(baseUrl()+"/users", new HttpEntity<>(json, h), Void.class);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        List<Map<String,Object>> users = getUsers();
        Map<String,Object> user1 = users.stream().filter(u -> "Donald".equals(u.get("username"))).findFirst().orElseThrow();
        UUID user1Id = UUID.fromString(String.valueOf(user1.get("id")));

        Map<String,Object> u2 = Map.of(
                "username", "Goofy",
                "email", "456@456"
        );
        ResponseEntity<Void> createU2 = exchange(HttpMethod.POST, baseUrl()+"/users", u2, Void.class);
        assertThat(createU2.getStatusCode().is2xxSuccessful()).isTrue();

        users = getUsers();
        assertThat(users.stream().map(u -> (String) u.get("username"))).contains("Donald", "Goofy");
        Map<String,Object> user2 = users.stream().filter(u -> "Goofy".equals(u.get("username"))).findFirst().orElseThrow();
        UUID user2Id = UUID.fromString(String.valueOf(user2.get("id")));

        UUID pollId = createPollForUser(user1Id,
                "One or two",
                List.of(
                        option("One", 1),
                        option("Two", 2)
                ));

        List<Map<String,Object>> polls = getPollsForUser(user1Id);
        assertThat(polls).anySatisfy(p -> assertThat(p.get("question")).isEqualTo("One or two"));

        Map<String,Object> poll = polls.stream().filter(p -> "One or two".equals(p.get("question"))).findFirst().orElseThrow();
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> options = (List<Map<String,Object>>) poll.get("voteOptions");
        UUID option1 = UUID.fromString(String.valueOf(options.get(0).get("id")));
        UUID option2 = UUID.fromString(String.valueOf(options.get(1).get("id")));

        vote(pollId, user2Id, option1);
        vote(pollId, user2Id, option2);

        List<Map<String,Object>> votes = getVotes(pollId);
        List<Map<String,Object>> votesForUser2 = new ArrayList<>();
        for (Map<String,Object> v : votes) {
            if (Objects.equals(String.valueOf(user2Id), String.valueOf(v.get("user")))) {
                votesForUser2.add(v);
            }
        }
        assertThat(votesForUser2).isNotEmpty();

        Map<String,Object> latestVoteForUser2 = votesForUser2.stream()
                .sorted((a,b) -> {
                    Instant ia = parseInstantOrNull(a.get("publishedAt"));
                    Instant ib = parseInstantOrNull(b.get("publishedAt"));
                    if (ia == null && ib == null) return 0;
                    if (ia == null) return -1;
                    if (ib == null) return 1;
                    return ia.compareTo(ib);
                })
                .reduce((first, second) -> second)
                .orElseThrow();

        assertThat(String.valueOf(latestVoteForUser2.get("voteOption"))).isEqualTo(String.valueOf(option2));

        ResponseEntity<Void> deletePoll = exchange(HttpMethod.DELETE, baseUrl()+"/polls/"+pollId, null, Void.class);
        assertThat(deletePoll.getStatusCode().is2xxSuccessful()).isTrue();

        List<Map<String,Object>> votesAfterDelete = getVotes(pollId);
        assertThat(votesAfterDelete).isEmpty();
    }

    private Map<String,Object> option(String caption, int order) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("caption", caption);
        m.put("presentationOrder", order);
        return m;
    }

    private UUID createPollForUser(UUID userId, String question, List<Map<String,Object>> options) {
        Map<String,Object> poll = new LinkedHashMap<>();
        poll.put("question", question);
        poll.put("publishedAt", Instant.now().toString());
        poll.put("validUntil", Instant.now().plusSeconds(3600).toString());
        poll.put("voteOptions", options);

        ResponseEntity<Void> resp = exchange(HttpMethod.POST, baseUrl()+"/polls/"+userId, poll, Void.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();

        List<Map<String,Object>> polls = getPollsForUser(userId);
        Optional<Map<String,Object>> created = polls.stream().filter(p -> Objects.equals(p.get("question"), question)).findFirst();
        assertThat(created).isPresent();
        return UUID.fromString(String.valueOf(created.get().get("id")));
    }

    private void vote(UUID pollId, UUID userId, UUID optionId) {
        String url = String.format("%s/polls/%s/votes", baseUrl(), pollId);

        Map<String,Object> vote = new LinkedHashMap<>();
        vote.put("user", userId.toString());
        vote.put("voteOption", optionId.toString());
        vote.put("publishedAt", Instant.now().toString());

        ResponseEntity<Void> resp = exchange(HttpMethod.POST, url, vote, Void.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private List<Map<String,Object>> getUsers() {
        var type = new ParameterizedTypeReference<List<Map<String,Object>>>() {};
        ResponseEntity<List<Map<String,Object>>> resp = rest.exchange(baseUrl()+"/users", HttpMethod.GET, null, type);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        return Objects.requireNonNull(resp.getBody());
    }

    private List<Map<String,Object>> getPollsForUser(UUID userId) {
        var type = new ParameterizedTypeReference<List<Map<String,Object>>>() {};
        ResponseEntity<List<Map<String,Object>>> resp = rest.exchange(baseUrl()+"/polls/"+userId, HttpMethod.GET, null, type);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        return Objects.requireNonNull(resp.getBody());
    }

    private List<Map<String,Object>> getVotes(UUID pollId) {
        var type = new ParameterizedTypeReference<List<Map<String,Object>>>() {};
        String url = String.format("%s/polls/%s/votes", baseUrl(), pollId); // <-- correct path

        try {
            ResponseEntity<List<Map<String,Object>>> resp =
                    rest.exchange(url, HttpMethod.GET, null, type);

            if (resp.getStatusCode().value() == 404) return List.of();
            assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
            return Objects.requireNonNull(resp.getBody());
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return List.of();
        }
    }


    private <T> ResponseEntity<T> exchange(HttpMethod method, String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        if (responseType != Void.class) {
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        }
        HttpEntity<?> entity = (method == HttpMethod.GET || method == HttpMethod.DELETE)
                ? new HttpEntity<>(headers)
                : new HttpEntity<>(body, headers);

        return rest.exchange(url, method, entity, responseType);
    }

    private <T> ResponseEntity<T> exchange(HttpMethod method, String url, Object body, ParameterizedTypeReference<T> typeRef) {
        HttpHeaders headers = new HttpHeaders();
        if (method != HttpMethod.GET && method != HttpMethod.DELETE) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = (method == HttpMethod.GET || method == HttpMethod.DELETE)
                ? new HttpEntity<>(headers)
                : new HttpEntity<>(body, headers);
        return rest.exchange(url, method, entity, typeRef);
    }


    private static Instant parseInstantOrNull(Object value) {
        if (value == null) return null;
        try {
            return Instant.parse(String.valueOf(value));
        } catch (Exception ignored) {
            return null;
        }
    }
}
