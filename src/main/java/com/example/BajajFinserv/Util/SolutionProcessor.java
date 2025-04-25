package com.example.BajajFinserv.Util;

import com.example.BajajFinserv.model.webHookResponse;

import java.util.*;

public class SolutionProcessor {
    public static List<Integer> solveNthLevel(Map<String, Object> data) {
        int n = (Integer) data.get("n");
        int findId = (Integer) data.get("findId");


        Object usersObject = data.get("users");
        List<Map<String, Object>> usersRaw = new ArrayList<>();

        if (usersObject instanceof List) {
            usersRaw = (List<Map<String, Object>>) usersObject;
        } else {
            System.err.println("Expected List<Map<String, Object>> for users, but found: " +
                    (usersObject != null ? usersObject.getClass().getName() : "null"));
            return Collections.emptyList();
        }


        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (Map<String, Object> user : usersRaw) {
            int id = (Integer) user.get("id");
            List<Integer> follows = (List<Integer>) user.get("follows");
            graph.put(id, follows);
        }


        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(findId);
        visited.add(findId);

        int level = 0;
        while (!queue.isEmpty() && level < n) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                for (int neighbor : graph.getOrDefault(current, List.of())) {
                    if (!visited.contains(neighbor)) {
                        queue.offer(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
            level++;
        }

        List<Integer> result = new ArrayList<>(queue);
        Collections.sort(result);
        return result;
    }

    public static List<List<Integer>> solve(webHookResponse response) {
        Map<String, Object> data = response.getData();


        Object usersObject = data.get("users");
        List<Map<String, Object>> usersRaw = new ArrayList<>();

        if (usersObject instanceof List) {
            usersRaw = (List<Map<String, Object>>) usersObject;
        } else {
            System.err.println("Expected List<Map<String, Object>>, but found: " + usersObject.getClass().getName());
            return Collections.emptyList();
        }

        Map<Integer, Set<Integer>> followsMap = new HashMap<>();
        for (Map<String, Object> user : usersRaw) {
            int id = (Integer) user.get("id");
            List<Integer> follows = (List<Integer>) user.get("follows");
            followsMap.put(id, new HashSet<>(follows));
        }

        List<List<Integer>> mutuals = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (int u1 : followsMap.keySet()) {
            for (int u2 : followsMap.get(u1)) {
                if (followsMap.containsKey(u2) && followsMap.get(u2).contains(u1)) {
                    String key = Math.min(u1, u2) + "-" + Math.max(u1, u2);
                    if (!seen.contains(key)) {
                        mutuals.add(List.of(Math.min(u1, u2), Math.max(u1, u2)));
                        seen.add(key);
                    }
                }
            }
        }
        return mutuals;
    }

}