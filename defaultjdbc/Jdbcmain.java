package defaultjdbc;
import java.util.*;
import java.sql.*;

public class Jdbcmain {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Scanner sc= new Scanner(System.in);
		//Create Operation for distributions table
		System.out.println("Enter the distribution of weights for different assignment categories:");
		Connection conn= JdbcConnection.createCon();
        Statement st= conn.createStatement();
		String newqr="CREATE TABLE weightdistributions(catg varchar(30) PRIMARY KEY, marks int)";       //Creating a table with fields as categories and weightage percentage
    	st.executeUpdate(newqr);
int weight;
		for(int count=100; count>0;) {
			String query="insert into weightdistributions values(?,?)";
			System.out.println("Test again");

			PreparedStatement pstmnt= conn.prepareStatement(query);
			String catgr=sc.nextLine();
			pstmnt.setString(1, catgr);
			weight= sc.nextInt();
			String other=sc.nextLine();
			while(count-weight<0) {
				System.out.println("Enter a value less than or equal to "+count);
			    weight= sc.nextInt();
				String another=sc.nextLine();

			    
			}
			System.out.println("Test");
			pstmnt.setInt(2,weight);
			pstmnt.executeUpdate();
			count-=weight;
		}
		
		
		//Read Operation for distribution tables
		
        Statement stmnt= conn.createStatement();
		System.out.println("Displaying the Weights of each assignment category");

        String queryy="select * from weightdistributions";
		ResultSet set=stmnt.executeQuery(queryy);
		while(set.next()) {
			System.out.println("Assignment Category: " + set.getString(1));
			System.out.println("Weight " + set.getInt(2));
			System.out.println("--------------");
		}
		
		
//Create Operations for Assignments table
		System.out.println("Enter assignment details of students:");
		System.out.println("--------------------------------");

		Connection connec= JdbcConnection.createCon();
        while(true) {
        	String assgquery= "insert into assignments(studentname, subject, assgcateg, dateofsubmission, points) values(?,?,?,?,?)";
			PreparedStatement pstmnt= connec.prepareStatement(assgquery);
			System.out.println("Enter student name");
			pstmnt.setString(1, sc.nextLine());
			System.out.println("Enter subject name");
			pstmnt.setString(2, sc.nextLine());
			System.out.println("Enter assignment category name");
			pstmnt.setString(3, sc.nextLine());
			System.out.println("Enter date of submission in YYYY-MM-DD format");
			pstmnt.setString(4, sc.nextLine());
			System.out.println("Enter the points");
			pstmnt.setInt(5, sc.nextInt());
			String another=sc.nextLine();
			int result= pstmnt.executeUpdate();
			if(result>1)
				System.out.println("Successfully added the assignment to the table");
			String ch;
        	do{
        		System.out.println("Do you want to add more assignments. Enter correct value either Y or N");
            	ch= sc.nextLine();

        	}while(!(ch.equals("N")||ch.equals("Y")));
        	if(ch.equals("N"))
        			break;
        }
		int choice;
		Statement s1= connec.createStatement();
		Statement s0= connec.createStatement();
		Statement s2= connec.createStatement();

int overallcount=0;
	 while(true) {
			System.out.println("Enter your choice: 1.Calculate the student average score per assignment category & overall rating for assigned subject(s).");
			System.out.println("2. Calculate Subject average score per assignment category & overall rating for assigned student(s).");
			System.out.println("3. Exit");
            
			choice= sc.nextInt();
			String dup=sc.nextLine();
			overallcount++;
			if (choice==1) {
				System.out.println("Enter the student name to calculate his rating");
				String studentName= sc.nextLine();
		        Statement ns= conn.createStatement();
		        String qr="SELECT DISTINCT subject FROM assignments WHERE studentname='"+studentName+"'";     //returns a list of unique subjects assigned to the particular student
		        ResultSet se= ns.executeQuery(qr);
		        while(se.next()) {
		        	String currentsub=se.getString(1);
		        	int a = currentsub.length();
		        	for(int i=0; i<currentsub.length(); i++)
					{
						if(currentsub.charAt(i)==' ') {
							a=i;
							break;
						}
					}
					String currentsubnew=currentsub.substring(0,a).concat(studentName).concat(String.valueOf(overallcount));
		        	String nq="CREATE TABLE "+currentsubnew+"(catg varchar(12), marks int)";    //Creating a table with name as subject and fields as raw assignment categories and their points
		        	s0.executeUpdate(nq);
		        	String subquery="insert into "+currentsubnew+" SELECT assgcateg, points FROM assignments WHERE studentname='"+studentName+"' AND subject='"+currentsub+"' ORDER BY assgcateg";
					PreparedStatement pst= connec.prepareStatement(subquery);
					pst.executeUpdate();
					System.out.println("-------------");
					System.out.println(currentsub);
					System.out.println("-------------");
                    double overallratingsub=0;
                    String b="select * from weightdistributions";
            		ResultSet s=s1.executeQuery(b);
            		while(s.next()) {
            			String ctg= s.getString(1);
            			double val=s.getInt(2);
            			String m= "SELECT * FROM "+currentsubnew+" WHERE catg LIKE '%"+ctg+"%'";   //returns the rows where assignment categories are same like project_1, project_2
        		        ResultSet subjectuniqueassgmnts= s2.executeQuery(m);
    		        	
                        double count=0, score=0;
        		        boolean notempty=false;
        		        while(subjectuniqueassgmnts.next()) {
        		            notempty=true;		        
        		        	count+= 1;
        		        	score+=subjectuniqueassgmnts.getInt(2);
        		        }
                        if(notempty) {
                        	
                        double average= score/count;
                        double ratingincateg= average*val/100;
                        overallratingsub+=ratingincateg;
                        	System.out.println("Score in Assignment Category: "+ctg+"= "+ratingincateg);
                        }

		        }
			System.out.println("Overall Rating in Subject= "+overallratingsub);
			System.out.println();
			System.out.println();

		        }
			}
			else if (choice==2){
				System.out.println("Enter the subject name to calculate all students' rating in that subject");
				String subjectName= sc.nextLine();
				
		        Statement ns1= conn.createStatement();
		        String qr="SELECT DISTINCT studentname FROM assignments WHERE subject='"+subjectName+"'";     //returns a list of unique students assigned to the particular subject
		        ResultSet se= ns1.executeQuery(qr);
		        while(se.next()) {
		        	String currentstdnt=se.getString(1);
		        	String currentstdnew=currentstdnt.concat(String.valueOf(overallcount));
					String nq="CREATE TABLE "+currentstdnew+"(catg varchar(12), marks int)";    //Creating a table with name as student and fields as raw assignment categories and their points
		        	s0.executeUpdate(nq);
		        	String subquery="insert into "+currentstdnew+" SELECT assgcateg, points FROM assignments WHERE studentname='"+currentstdnt+"' AND subject='"+subjectName+"' ORDER BY assgcateg";
					PreparedStatement pst= connec.prepareStatement(subquery);
					pst.executeUpdate();
					System.out.println("-------------");
					System.out.println(currentstdnt);
					System.out.println("-------------");
                    double overallratingstud=0, ratingincateg=0;
                    String b="select * from weightdistributions";
            		ResultSet s=s1.executeQuery(b);
            		while(s.next()) {
            			String ctg= s.getString(1);
            			double val=s.getInt(2);
            			String m= "SELECT * FROM "+currentstdnew+" WHERE catg LIKE '%"+ctg+"%'";   //returns the rows where assignment categories are same like project_1, project_2
        		        ResultSet subjectuniqueassgmnts= s2.executeQuery(m);
    		        	
                        double count=0, score=0;
        		        boolean notempty=false;
        		        while(subjectuniqueassgmnts.next()) {
        		            notempty=true;		        
        		        	count+= 1;
        		        	score+=subjectuniqueassgmnts.getInt(2);
        		        }
                        if(notempty) {
                        	
                        double average= score/count;
                        ratingincateg= average*val/100;
                        overallratingstud+=ratingincateg;
                        	System.out.println("Score in Assignment Category: "+ctg+"= "+ratingincateg);
                        }

		        }
			System.out.println("Overall Rating of student in Subject- "+subjectName+ "="+overallratingstud);
			System.out.println();
			System.out.println();

		        }


			}
			else if (choice==3)
				break;
			else {
				System.out.println("Invalid Entry, please try again");
				
				
			}
	 }
 
	}		
}
