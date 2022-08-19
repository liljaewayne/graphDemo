package com.leetcode.q310;

import java.util.*;

// FIXME: 换根dp
class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();

//        List<Integer> minHeightTrees = solution.findMinHeightTrees(4, new int[][]{
//                new int[]{1, 0},
//                new int[]{1, 2},
//                new int[]{1, 3}});

        List<Integer> minHeightTrees = solution.findMinHeightTrees(6, new int[][]{
                new int[]{3, 0},
                new int[]{3, 1},
                new int[]{3, 2},
                new int[]{3, 4},
                new int[]{5, 4}
        });


        System.out.println(minHeightTrees);
    }

    private HashMap<Integer, Set<Integer>> adjDic;

    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        buildG(n, edges);

        List<Integer> roots = new ArrayList<>();

        // 以n为起始dfs, 计算高度
        int minDepth = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {

            boolean[] visited = new boolean[n];

            int depth = dfsDepth(i, visited);

            if (depth < minDepth) {
                roots.clear();
                roots.add(i);
                minDepth = depth;
            } else if (depth == minDepth) {
                roots.add(i);
            }

        }


        return roots;
    }

    private int dfsDepth(int root, boolean[] visited) {

        if (visited[root]) {
            return 1;
        }

        visited[root] = true;
        Set<Integer> adjs = adjDic.get(root);


        int maxD = 0;
        for (Integer adj : adjs) {
            int d = dfsDepth(adj, visited);
            maxD = Math.max(d, maxD);
        }

        return maxD + 1;
    }


    private void buildG(int n, int[][] edges) {
        adjDic = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adjDic.put(i, new HashSet<>());
        }


        for (int i = 0; i < edges.length; i++) {
            int[] edge = edges[i];
            int lNode = edge[0];
            int rNode = edge[1];

            adjDic.get(lNode).add(rNode);
            adjDic.get(rNode).add(lNode);
        }
    }

    private void initMinDepthDic(int n, int[][] edges) {

    }

}