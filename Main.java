
import java.io.*;
import java.util.*;

class Main {
        
        
    
    static char routes[][]; // array to initially store routes by input
	static ArrayList<Character> stations=new ArrayList<Character>(); //stores all stations
	static ArrayList<ArrayList<String>> passengers = new ArrayList<>();
	static int pod_src; //starting station
	static ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
	

    public static void write_file(String msg1,String[] msg2,String msg3){
	try {
	String filename= "Data.txt";
	FileWriter fw = new FileWriter(filename,true); 
    	fw.write("--------------------"+msg1+"--------------------\n\n");
	
	if (msg2!=null)
	for(String i:msg2)
	fw.write(i+" \n");

	if (!msg3.isEmpty()){
	fw.write("\n"+msg3+" \n");
	if (passengers.size()>0){
	for(int i=0;i<passengers.size();i++)
	fw.write((i+1)+". "+ passengers.get(i)+"\n");}
	
	else
	fw.write("--->  0");
	fw.write("\n\n\n");}
    	fw.close();
	}
	catch (IOException e) {
      		System.out.println("An error occurred.");
      		e.printStackTrace();}
}    
    public static void cmdcheck(String cmd[]){
        
            int pc=0;//passenger count
            
            Scanner sc=new Scanner(System.in);
            String[] getval;
            if (Arrays.asList(cmd).contains("ADD_PASSENGER")){
                pc=Integer.parseInt(cmd[1]);
                for(int i=0;i<pc;i++)
                
                passengers.add(new ArrayList<>(Arrays.asList(sc.nextLine().split(" "))));
                //System.out.println(passengers);
	write_file("Passenger Added",null,"Passengers in queue:");
            
            } 
            
            else if(Arrays.asList(cmd).contains("START_POD")){
                int temp, c=0;
                int limit=Integer.parseInt(cmd[1]);
	String pat[]=new String[limit];//contains minimal path of current passenger in pod	
                if (limit<=passengers.size()){
                while(limit>0){
                    int max=Integer.parseInt(passengers.get(0).get(1)); //store max age
                    int ind=0;
                    if (passengers.size()>1){
                    for(int i=0;i<passengers.size();i++){
                        temp=Integer.parseInt(passengers.get(i).get(1));
		                if (temp>max){
		                    max=temp;
		                    ind=i;
                        }
                    }
                    }
                    
                int des=stations.indexOf(passengers.get(ind).get(2).charAt(0));
                System.out.print(passengers.get(ind).get(0)+" ");
                pat[c]=(passengers.get(ind).get(0))+" travelled "+print_paths(adj,stations.size(),pod_src,des);
                passengers.remove(ind);
                limit--;    
	c++;
            
                }
	write_file("Pod Started",pat,"Remaining Passengers in Queue");
            }    
            else{
            System.out.println("Only "+passengers.size()+" passengers are in queue now");
            System.out.println("Please enter ample passenger count ");}
                
            }
            else if(Arrays.asList(cmd).contains("PRINT_Q")){
                if (passengers.size()>0){
	System.out.println(passengers.size());
                for(int i=0;i<passengers.size();i++)
                System.out.println(passengers.get(i).get(0)+" "+passengers.get(i).get(1));
                }
                else
                System.out.println("No passengers currently in queue!!!");
            }
            else
            System.out.println("No such command!!");
        }
    
	//function to add route between 2 stations
	static void add_route(ArrayList<ArrayList<Integer>> adj, int src, int dest){
		adj.get(src).add(dest);
		adj.get(dest).add(src);
	}

	// function to find minimal path between source and destination
	static void find_paths(ArrayList<ArrayList<Integer>> paths, ArrayList<Integer> path,
					ArrayList<ArrayList<Integer>> parent, int n, int u) {
		
		if (u == -1) {
			paths.add(new ArrayList<>(path));
			return;
		}

		
		for (int par : parent.get(u)) {

			
			path.add(u);

			// Recursive call for its parent
			find_paths(paths, path, parent, n, par);

			// Remove the current station
			path.remove(path.size()-1);
		}
	}

	// Function to performs bfs from given station
	static void bfs(ArrayList<ArrayList<Integer>> adj, ArrayList<ArrayList<Integer>> parent,
			int n, int start) {
	
		// dist will contain shortest distance
		// from start to every other vertex
		int[] dist = new int[n];
		Arrays.fill(dist, Integer.MAX_VALUE);

		Queue<Integer> q = new LinkedList<>();

		// Insert source vertex in queue and make
		// its parent -1 and distance 0
		q.offer(start);
		
		parent.get(start).clear();
		parent.get(start).add(-1);
	
		dist[start] = 0;

		// Until Queue is empty
		while (!q.isEmpty()) {
			int u = q.poll();
			
			for (int v : adj.get(u)) {
				if (dist[v] > dist[u] + 1) {

					//when minimal path found erase prevois parents and add this
					dist[v] = dist[u] + 1;
					q.offer(v);
					parent.get(v).clear();
					parent.get(v).add(u);
				}
				else if (dist[v] == dist[u] + 1) {

					
					parent.get(v).add(u);
				}
			}
		}
	}

	//function to print the route from source to destination station
	static String print_paths(ArrayList<ArrayList<Integer>> adj, int n, int start, int end){
		ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<ArrayList<Integer>> parent = new ArrayList<>();
		
		for(int i = 0; i < n; i++){
			parent.add(new ArrayList<>());
		}
	
		// Function call to bfs
		bfs(adj, parent, n, start);

		// Function call to find_paths
		find_paths(paths, path, parent, n, end);
		String pat="";
		for (ArrayList<Integer> v : paths) {

			// Since paths contain each
			// path in reverse order,
			// so reverse it
			Collections.reverse(v);

			// Print node for the current path
			
			for (int u : v){
				System.out.print(stations.get(u) + " ");
				pat+=(stations.get(u) + " ");
			}
			System.out.println();
			break;
			
		}return pat;

	}
    
    
    public static void main (String[] args) //Main
	{
	Date date = new Date(); // for writing in text file

	int n; // no. of routes    
    Scanner inp = new Scanner(System.in);	    
	String []a=inp.nextLine().split(" ");
	String temp;
	
	if (a[0].equals("INIT")){ // checks if INIT is first initialized
                
        n=Integer.parseInt(a[1]);
        routes=new char[n][2]; 
        
        
        for(int i=0;i<n;i++){
        temp=inp.nextLine();
        routes[i][0]=temp.charAt(0);// storing all the routes in routes array
        routes[i][1]=temp.charAt(2);
                
        // storing all unique stations
        if (!(stations.contains(temp.charAt(0)))){
                        
            stations.add(temp.charAt(0));
                        
            } 
        if (!(stations.contains(temp.charAt(2)))){
            stations.add(temp.charAt(2));
                        
            }   
                    
                
        }
        
        int station_count=stations.size();
        for(int i = 0; i <station_count; i++){// creating stations 2d ArrayList
		    adj.add(new ArrayList<>());
        }
        	
        int  s1,s2;
	    // calling function add_routes to establish route between stations
	    for(int i=0;i<routes.length;i++){
	       s1=stations.indexOf(routes[i][0]);
	        s2=stations.indexOf(routes[i][1]);
	        add_route(adj, s1, s2);
	    }
	    pod_src=stations.indexOf(a[2].charAt(0));
	    write_file(date.toString(),null,"");
            
    }    
	  
    else{
            System.out.println("Please initialize the system with \'INIT\' command first");
        }
        
       
    temp=inp.nextLine();    	
	while(!temp.isEmpty()){
		    cmdcheck(temp.split(" "));
		    temp=inp.nextLine();
		}
		    
	}

	}






