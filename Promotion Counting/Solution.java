//Shreyan Jaiswal, January 2017
//Problem at http://usaco.org/index.php?page=viewproblem2&cpid=696

import java.io.*;
import java.lang.*;
import java.util.*;

public class promote {
	static long[] prof;
	static int[] ans;
	static Set[] childs;
	static int[] depth, startTime, endTime;
	static Integer[] rating;
	static int N;
	public static void main(String args[]) throws IOException{
		BufferedReader f = new BufferedReader(new FileReader("promote.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("promote.out")));
		N = Integer.parseInt(f.readLine());
		prof = new long[N+1];
		ans = new int[N+1];
		startTime = new int[N+1]; endTime = new int[N+1];
		childs = new HashSet[N+1]; childs[0]=null;
		prof[0] = Long.MAX_VALUE;
		for(int i =1; i<=N; i++){
			prof[i] = Long.parseLong(f.readLine());
			childs[i]=new HashSet<Integer>();
		};
		for(int i =2; i<=N; i++){
			childs[Integer.parseInt(f.readLine())].add(i);
		}
		fill_inorder(1,0);
		computeRank();
		SegmentTree st = new SegmentTree(N+1); // indexed by startTime
		for(int i: rating){ //highest rated first
			if(i>N || i<0) continue;
			ans[i]=st.sum(startTime[i], endTime[i]);
			st.update(startTime[i], 1);
			//st.pr();
		}
		
		//System.out.println();
		for(int i=1; i<=N; i++) out.println(ans[i]);
		
		f.close();
		out.close();
		System.exit(0);
	}
	static int fill_inorder(int id, int curTime){
		if (startTime[id] != 0 && id!=0) return curTime;
		startTime[id]=++curTime;
		for(Object child: childs[id]){
			curTime = fill_inorder((int) child, curTime);
		}
		endTime[id]=curTime+1;
		return curTime;
	}
	static void computeRank(){
		rating = new Integer[N+1];
		rating[0]=0;
		for(int i =1; i<=N; i++){
			rating[i] = i;
		}
		//for(int i=1; i<=N; i++) System.out.print(rating[i]+" ");System.out.println();
		Arrays.sort(rating, new Comparator<Integer>(){
			public int compare(Integer o1, Integer o2){
				return prof[o1]>prof[o2]?-1:1;};
		});
		
	}
	
	static class SegmentTree{
		int sum[], l;
		public SegmentTree(int[] a){
			l = a.length;
			sum = new int[2*a.length];
			for(int i = 0; i < a.length; i++){
				sum[i+a.length] = a[i];
			}
			for(int i = a.length-1; i >=1; i--){
				sum[i] = sum[2*i]+ sum[2*i+1];
			}
		}
		public SegmentTree(int len){
			l = len;
			sum = new int[2*len];
		}
		public int get(int i){
			return sum[i+l];
		}
		public void update(int i, int delta){
			int idx =  i+l;
			sum[idx] += delta;
			while (idx > 1) {
				idx /= 2;
				sum[idx]=(sum[2*idx]+ sum[2*idx+1]);
	        }
		}
		public int sum(int left, int right){
			left+=l; right+=l;
			int E = 0;
			while(left<right){
				if ((left & 1) == 1) {
	                E+=sum[left];
	                left++;
	            }
	            if ((right & 1) == 1) {
	                right--;
	                E+=sum[right];
	            }
	            left >>= 1;
	            right >>= 1;
			}
			return E;
		}
		public void pr(){
			for(int k =1; k<sum.length; k++) System.out.print(sum[k]+(k==sum.length-1?"\n":" "));
		}
	}
}
