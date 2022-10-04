import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Polynomial {
	double coefficients[];
	int exponents[];
	
	Polynomial(){
		coefficients=new double[1];
		coefficients[0]=0;
		exponents=new int[1];
		exponents[0]=0;
	}
	
	Polynomial(double[] a, int[] b){
		this.coefficients=a;
		this.exponents=b;
	}
	
	public int getNumElements(String s) {
		int count=1;
		if (s.charAt(0)=='-')
			count=0;
		for(int i=0;i<s.length();i++) 
			if(s.charAt(i)=='+'||s.charAt(i)=='-')
				count++;
		return count;
	}
	
	public String[] get_elements(String s){
		String[] all=new String[getNumElements(s)];
		String[] one=s.split("\\+");
		int index=0;
		for(String i:one) {
			if(!i.contains("-")) {
				all[index]=i;
				index++;
			}
			else if(i.split("\\-")[0]!="") {
				all[index]=i.split("\\-")[0];
				index++;
				for(int j=1;j<getNumElements(i);j++) {
					all[index]="-"+i.split("\\-")[j]; 
					index++;
				}
			}
			else {
				for(int j=0;j<getNumElements(i);j++) {
					all[index]="-"+i.split("\\-")[j+1];
					index++;
					}
			}
		}
		return all;
	}
	
	public double[] set_coef(String[] terms) {
		double arr[]=new double[terms.length];
		for(int i=0;i<terms.length;i++) {
			if(terms[i].equals("x"))
				arr[i]=1;
			else {
				String cur=terms[i].split("x")[0];
				if(cur.equals("-"))
					arr[i]=-1;
				else
					arr[i]=Double.parseDouble(cur);
				}
		}
		return arr;
	}
	
	public int[] set_expo(String[] terms) {
		int arr[]=new int[terms.length];
		for(int i=0;i<terms.length;i++) {
			if(!terms[i].contains("x"))
				arr[i]=0;
			else if(terms[i].split("x").length==1) {
				arr[i]=1;
			}
			else if((terms[i].equals("x")))
				arr[i]=1;
			else
				arr[i]=Integer.parseInt(terms[i].split("x")[1]);
		}
		return arr;
	}
	
	Polynomial(File f){
		try {
			Scanner scan=new Scanner(f);
			String[] elements;
			if(scan.hasNextLine()) {
				elements=get_elements(scan.nextLine());
				scan.close();
				this.coefficients=set_coef(elements);
				this.exponents=set_expo(elements);
			}
			else {
				coefficients=new double[1];
				coefficients[0]=0;
				exponents=new int[1];
				exponents[0]=0;
			}
		}catch(IOException e) {
			System.out.println("File not found");
		}
	}
	
	public static boolean contains(int a, int[] expo) {
		for(int i: expo) {
			if (a==i)
				return true;
		}
		return false;
	}
	
	public static int get_index(int a, int[] expo) {
		int index=0;
		for(int i:expo) {
			if (a==i)
				return index;
			index++;
		}
		return index;
	}
	
	
	
	public Polynomial add(Polynomial p){
		int size=0;
		for(int i:exponents) {
			for(int j:p.exponents) {
				if (i==j)
					size++;
			}
		}
		size = exponents.length + p.exponents.length - size;
		double temp1[]=new double[size];
		int temp2[]=new int[size];
		for(int i=0; i<size;i++) {
			temp1[i]=0;
			temp2[i]=-1;
		}
		int num=0;
		for(int j:exponents) {
			temp2[num]=j;
			num++;
		}
		for(int j:p.exponents) {
			if (!contains(j,temp2)) {
				temp2[num]=j;
				num++;
			}
		}
	
		
		for(int i=0;i<size;i++) {
			int a=temp2[i];
			if(contains(a,this.exponents))
				temp1[i]+=this.coefficients[get_index(a,this.exponents)];
			if(contains(a,p.exponents))
				temp1[i]+=p.coefficients[get_index(a,p.exponents)];
		}
		Polynomial n=new Polynomial(temp1, temp2);
		return n;
	}
	
	public Polynomial multiply(Polynomial p) {
		int[] expo;
		double[] coef;
		double[] temp1=new double[p.coefficients.length];
		int[] temp2= new int[p.exponents.length];
		for(int i=0; i<temp1.length;i++) {
			temp1[i]=0;
			temp2[i]=0;
		}
		Polynomial n=new Polynomial(temp1,temp2);
		for(int i=0;i<this.exponents.length;i++) {
			expo=new int[p.exponents.length];
			coef= new double[p.coefficients.length];
			for(int j=0;j<p.exponents.length;j++) {
				expo[j]=exponents[i]+p.exponents[j];
				coef[j]=coefficients[i]*p.coefficients[j];
			}
			if (i==0)
				n=new Polynomial(coef,expo);
			else {
				Polynomial m = new Polynomial(coef, expo);
				n=n.add(m);
			}
		}
		return n;
	}
	
	public void saveToFile(String fileName) {
		String builder="";
		for(int i=0; i<exponents.length; i++) {
			if(coefficients[i]!=0) {
				String s=String.valueOf(coefficients[i]);
				if(s.charAt(s.length()-1)=='0'&&s.charAt(s.length()-2)=='.')
					s= String.valueOf(s).split("\\.")[0];
				if(coefficients[i]==1||coefficients[i]==-1)
					s="";
				if(coefficients[i]==-1)
					s="-";
				if(i==0) {
					if(exponents[i]==0) {
						builder+=s;
					}
					else {
						builder+=s+"x"+String.valueOf(exponents[i]);
					}
				}
				else {
					if(exponents[i]==0&&coefficients[i]<0) {
						builder+=s;
					}
					else if(exponents[i]==0&&coefficients[i]>=0) {
						builder+="+"+s;
					}
					else if(coefficients[i]>=0) {
						builder+="+"+s+"x"+String.valueOf(exponents[i]);
					}
					else {
						builder+=s+"x"+String.valueOf(exponents[i]);
					}
				}
			}
		}
		try {
			File file=new File(fileName);
			if (!file.exists())
				file.createNewFile();
			FileWriter f=new FileWriter(file);
			f.write(builder);
			f.close();
		} catch(IOException e) {
			System.out.println("error");
		}
	}
	
	public double evaluate(double input){
		double result=0;
		for(int i=0; i<coefficients.length; i++){
			result+=coefficients[i]*Math.pow(input,exponents[i]);
		}
		return result;
	}

	public boolean hasRoot(double input){
		return evaluate(input)==0;
	}
	
	public void Polyprint() {
		System.out.println("COEFFICIENTS");
		for (double i:coefficients) {
			System.out.println(i);
		}
		System.out.println("EXPONENTS");
		for (int i:exponents) {
			System.out.println(i);
		}
		
		System.out.println("");
	}
}

