package spoj.dynamic_programming.mtree;

// http://www.spoj.com/problems/MTREE/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MTREE_5_Final {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		verticesCount = Integer.parseInt(br.readLine().trim());

		vertexFrom = new int[(verticesCount - 1) * 2];
		vertexTo = new int[(verticesCount - 1) * 2];
		edgeWeight = new int[(verticesCount - 1) * 2];

		String[] parts;
		for (int i = 0; i < vertexFrom.length; i += 2) {
			parts = br.readLine().trim().split(" ", -1);

			vertexFrom[i] = Integer.parseInt(parts[0]);
			vertexTo[i] = Integer.parseInt(parts[1]);
			edgeWeight[i] = Integer.parseInt(parts[2]);

			vertexFrom[i + 1] = vertexTo[i];
			vertexTo[i + 1] = vertexFrom[i];
			edgeWeight[i + 1] = edgeWeight[i];
		}

		calculate();
		System.out.println(result);
	}

	static final long MOD = 1000000007;

	// (2 * _2_INV_MOD) == 1 (mod MOD)
	static final long _2_INV_MOD = (MOD + 1) / 2;

	// sorted array with all edges (duplicated in both directions)
	// edges sorted by index of outgoing vertex
	static int[] vertexFrom;
	static int[] vertexTo;
	static int[] edgeWeight;

	static int verticesCount;

	// index in bidirEdges - of the current unvisited edge, which outcomes from given vertex
	static int[] unvisitedEdgeIdx;

	// if given vertex is visited
	static boolean[] vertexVisited;

	// forward: sum of all prefix-paths weights, ending in given vertex
	static long[] fwd;

	// forward: sum of all paths weights, which ends before given vertex
	static long[] exclCurr;

	// backward: sum of all prefix-paths weights, ending in given vertex
	static long[] bkwd;

	// backward: sum of all squared prefix-paths weights, ending in given vertex
	static long[] bkwdSqr;

	// amount of visited children, visited from given vertex
	static int[] visitedChildrenCnt;

	// sum of all path weights of the tree
	// (path weight is a product of all edge weights across the path)
	static long result;

	static int[] stack;
	static int stackTop;

	static void calculate() {

		result = 0;
		unvisitedEdgeIdx = new int[verticesCount + 1];
		vertexVisited = new boolean[verticesCount + 1];
		fwd = new long[verticesCount + 1];
		exclCurr = new long[verticesCount + 1];
		bkwd = new long[verticesCount + 1];
		bkwdSqr = new long[verticesCount + 1];
		visitedChildrenCnt = new int[verticesCount + 1];
		stack = new int[verticesCount + 1];
		stackTop = -1;

		sortEdgesByFrom();

		Arrays.fill(unvisitedEdgeIdx, Integer.MAX_VALUE);
		for (int i = 0; i < vertexFrom.length; i++) {
			unvisitedEdgeIdx[vertexFrom[i]] =
					(unvisitedEdgeIdx[vertexFrom[i]] < i) ? unvisitedEdgeIdx[vertexFrom[i]] : i;
		}

		int startEdge = findLeafEdge();

		unvisitedEdgeIdx[vertexFrom[startEdge]]++;
		vertexVisited[vertexFrom[startEdge]] = true;
		fwd[vertexFrom[startEdge]] = 0;
		exclCurr[vertexFrom[startEdge]] = 0;

		traverseTreeDFS(startEdge);
	}

	static void sortEdgesByFrom() {
		int[] newVertexFrom = new int[(verticesCount - 1) * 2];
		int[] newVertexTo = new int[(verticesCount - 1) * 2];
		int[] newEdgeWeight = new int[(verticesCount - 1) * 2];

		int[] valCount = new int[verticesCount + 1];
		for (int i = 0; i < vertexFrom.length; i++) {
			valCount[vertexFrom[i]]++;
		}
		valCount[0] = valCount[0] - 1;
		for (int i = 1; i < valCount.length; i++) {
			valCount[i] = valCount[i - 1] + valCount[i];
		}
		for (int i = vertexFrom.length - 1; i >= 0; i--) {
			newVertexFrom[valCount[vertexFrom[i]]] = vertexFrom[i];
			newVertexTo[valCount[vertexFrom[i]]] = vertexTo[i];
			newEdgeWeight[valCount[vertexFrom[i]]] = edgeWeight[i];
			valCount[vertexFrom[i]]--;
		}
		vertexFrom = newVertexFrom;
		vertexTo = newVertexTo;
		edgeWeight = newEdgeWeight;
	}

	static void traverseTreeDFS(int currEdge) {

		int currVertex;
		int prevVertex;
		boolean hasUnvisitedChildren;

		stack[++stackTop] = currEdge;

		LOOP : while (stackTop > -1) {

			currEdge = stack[stackTop--];
			currVertex = vertexTo[currEdge];

			if (!vertexVisited[currVertex]) {

				prevVertex = vertexFrom[currEdge];

				fwd[currVertex] = ((fwd[prevVertex] * edgeWeight[currEdge]) + edgeWeight[currEdge]) % MOD;
				exclCurr[currVertex] = (fwd[prevVertex] + exclCurr[prevVertex]) % MOD;
				vertexVisited[currVertex] = true;

				hasUnvisitedChildren = false;
				while ((unvisitedEdgeIdx[currVertex] < vertexFrom.length)
						&& (vertexFrom[unvisitedEdgeIdx[currVertex]] == currVertex)) {

					if (!vertexVisited[vertexTo[unvisitedEdgeIdx[currVertex]]]) {
						hasUnvisitedChildren = true;
						break;
					}
					unvisitedEdgeIdx[currVertex]++;
				}
				if (!hasUnvisitedChildren) {
					bkwd[currVertex] = edgeWeight[currEdge];
					result = (result + fwd[currVertex] + exclCurr[currVertex]) % MOD;
					continue LOOP;
				}

				bkwd[currVertex] = 0;
				bkwdSqr[currVertex] = 0;
				visitedChildrenCnt[currVertex] = 0;

				int childEdge = unvisitedEdgeIdx[currVertex];
				while ((childEdge < vertexFrom.length)
						&& (vertexFrom[childEdge] == currVertex)) {

					if (!vertexVisited[vertexTo[childEdge]]) {
						visitedChildrenCnt[currVertex]++;
						stack[++stackTop] = currEdge;
						stack[++stackTop] = childEdge;
						continue LOOP;
					}
					unvisitedEdgeIdx[currVertex]++;
					childEdge = unvisitedEdgeIdx[currVertex];
				}

				if (visitedChildrenCnt[currVertex] > 1) {
					result = (result - ((fwd[currVertex] + exclCurr[currVertex]) * (visitedChildrenCnt[currVertex] - 1))) % MOD;
					if (result < 0) {
						result += MOD;
					}
					result = (result + ((((((bkwd[currVertex] * bkwd[currVertex]) - bkwdSqr[currVertex])) % MOD) * _2_INV_MOD))) % MOD;
					if (result < 0) {
						result += MOD;
					}
				}

				bkwd[currVertex] = ((bkwd[currVertex] * edgeWeight[currEdge]) + edgeWeight[currEdge]) % MOD;

			} else {

				int childEdge = unvisitedEdgeIdx[currVertex];
				int childVertex = vertexTo[childEdge];

				bkwd[currVertex] = (bkwd[currVertex] + bkwd[childVertex]) % MOD;
				bkwdSqr[currVertex] = ((bkwd[childVertex] * bkwd[childVertex]) + bkwdSqr[currVertex]) % MOD;

				unvisitedEdgeIdx[currVertex]++;
				childEdge = unvisitedEdgeIdx[currVertex];

				while ((childEdge < vertexFrom.length) &&
						(vertexFrom[childEdge] == currVertex)) {

					if (!vertexVisited[vertexTo[childEdge]]) {
						visitedChildrenCnt[currVertex]++;
						stack[++stackTop] = currEdge;
						stack[++stackTop] = childEdge;
						continue LOOP;
					}
					unvisitedEdgeIdx[currVertex]++;
					childEdge = unvisitedEdgeIdx[currVertex];
				}

				if (visitedChildrenCnt[currVertex] > 1) {
					result = (result - ((fwd[currVertex] + exclCurr[currVertex]) * (visitedChildrenCnt[currVertex] - 1))) % MOD;
					if (result < 0) {
						result += MOD;
					}
					result = (result + ((((((bkwd[currVertex] * bkwd[currVertex]) - bkwdSqr[currVertex])) % MOD) * _2_INV_MOD))) % MOD;
					if (result < 0) {
						result += MOD;
					}
				}

				bkwd[currVertex] = ((bkwd[currVertex] * edgeWeight[currEdge]) + edgeWeight[currEdge]) % MOD;
			}
		}
	}

	static int findLeafEdge() {
		for (int i = 1; i < fwd.length; i++) {
			int idx = unvisitedEdgeIdx[i];
			if ((idx == (vertexFrom.length - 1))
					|| (vertexFrom[idx] != vertexFrom[idx + 1])) {

				return idx;
			}
		}
		throw new RuntimeException("Unreachable code");
	}
}
