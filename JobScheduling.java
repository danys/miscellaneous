package assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobScheduling
{
	//An O(N*log(N)) greedy algorithm to schedule jobs
	//Two strategies are provided in the Job classe's compare methods:
	//Strategy 1: order jobs by their weight/length ratio (this is optimal)
	//Strategy 2: order jobs by their weigth-length difference (this may or may not yield an optimal solution)
	public static class Job implements Comparable<Job>
	{
		private int weight;
		private int length;
		private int wldifference;
		private double wlratio;
		
		public Job(int weight, int length)
		{
			this.weight = weight;
			this.length = length;
			this.wldifference = weight-length;
			this.wlratio = (double)weight/(double)length;
		}
		
		public int getDiff()
		{
			return wldifference;
		}
		
		public double getRatio()
		{
			return wlratio;
		}
		
		public int getLength()
		{
			return length;
		}
		
		public int getWeight()
		{
			return weight;
		}
		
		/*@Override
		public int compareTo(Job o)
		{
			if (this.wldifference==o.wldifference)
			{
				if (this.weight<o.weight) return 1;
				else if (this.weight>o.weight) return -1;
				else return 0;
			}
			else if (this.wldifference<o.wldifference) return 1;
			else return -1;
		}*/
		
		@Override
		public int compareTo(Job o)
		{
			if (this.wlratio==o.wlratio) return 0;
			else if (this.wlratio<o.wlratio) return 1;
			else return -1;
		}
	}
	
	public static void main(String args[])
	{
		BufferedReader br = null;
		String line;
		String linestr[];
		int njobs,linecounter,weight,length;
		List<Job> jobs;
		Job job;
		long wtime;
		int time;
		try
		{
			FileReader fr = new FileReader("C:\\Users\\Dany\\Downloads\\jobs.txt");
			br = new BufferedReader(fr);
			line = br.readLine();
			if (line.isEmpty())
			{
				System.out.println("File must begin with a line providing the number of jobs");
				System.exit(0);
			}
			njobs = Integer.parseInt(line);
			linecounter=0;
			jobs = new ArrayList<Job>();
			line = br.readLine();
			while((line!=null) && (linecounter<njobs))
			{
				linestr = line.split("\\s+");
				if (linestr.length!=2)
				{
					System.out.println("Every line except the first must contain two space-separated integers!");
					System.exit(0);
				}
				weight = Integer.parseInt(linestr[0]);
				length = Integer.parseInt(linestr[1]);
				job = new Job(weight,length);
				jobs.add(job);
				line = br.readLine();
			}
			Collections.sort(jobs);
			wtime=0;
			time=0;
			for(Job j: jobs)
			{
				time += j.length;
				wtime += time*j.weight;
			}
			System.out.println("Total weighted completion time = "+wtime);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found!");
		}
		catch(IOException e)
		{
			System.out.println("File not found!");
		}
		catch(NumberFormatException e)
		{
			System.out.println("Input file contained a line entry that could not be converted into an integer number.");
		}
		finally
		{
			if (br!=null)
			try
			{
				br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
