/*
Shreyan Jaiswal, February 2017
*/
import java.io.*;
import java.lang.*;
import java.util.*;


public class mincross {
	static int N;
	
	public static void main(String args[]) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("mincross.in"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("mincross.out")));
		N = Integer.parseInt(br.readLine());
		int[] a =new int[N+1];
		List<Integer> r = new ArrayList<Integer>(); r.add(0);
		long xs = 0;
		for(int n=1; n <= N; n++){
			a[Integer.parseInt(br.readLine())]=n;
			r.add(0);
		}
		for(int n=1; n<=N; n++){
			System.out.print(a[n] + " ");
		}
		for(int n=1; n <= N; n++){
			r.set(n, a[Integer.parseInt(br.readLine())]);
		}
		System.out.println(a + " " + r);
		
		//count crosses k=0
		PartialSumTree fw = new PartialSumTree(1, N+1);
		for(int i =1; i <=N; i++){
			xs += fw.getQuery(r.get(i), N+1);
			fw.updateValue(r.get(i), 1);
		}
		
		long ret = xs;
		for(int i = N; i>=1; i--){
			xs  += (2*r.get(i) - (N+1));
			System.out.println(i + " " + xs);
			ret =  Math.min(ret, xs);
		}
		
		out.println((ret<0)?0:ret);
		br.close();
		out.close();
		System.exit(0);
	}
	static class PartialSumTree {
		int lo, hi, mid, sum;
		PartialSumTree left, right;
		PartialSumTree(int l, int r){
			lo=l;
			hi=r;
			mid=(lo + hi)/2;
			sum=0;
			if (mid == lo)
				left = right = null;
			else {
				left = new PartialSumTree(lo, mid);
				right = new PartialSumTree(mid, hi);
			}
		}
		void updateValue(int idx, int delta) {
			if (idx < lo || idx >= hi) return;
			sum += delta;
			if (mid != lo) {
				(idx < mid ? left : right).updateValue(idx, delta);
			}
		}
		//get partial sum from A to B - 1
		int getQuery(int A, int B) {
			if (A >= hi || B <= lo) return 0;
			if (A <= lo && B >= hi) return sum;
			return left.getQuery(A, B) + right.getQuery(A, B);
		}
	}
	
}
