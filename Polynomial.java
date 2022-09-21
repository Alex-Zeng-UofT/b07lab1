
public class Polynomial{
	double arr[];
	
	Polynomial(){
		arr=new double[1];
		arr[0]=0;
	}
	
	Polynomial(double[] a){
		this.arr=a;
	}

	public Polynomial add(Polynomial p){
		double temp[];
		if(arr.length>p.arr.length){
			temp=arr;
			for(int i=0; i<p.arr.length; i++){
		 		temp[i]+=p.arr[i];
			}
		}
		else{
			temp=p.arr;
			for(int i=0; i<arr.length; i++){
		 		temp[i]+=arr[i];
			}
		}
		Polynomial n=new Polynomial(temp);
		return n;
	}

	public double evaluate(double input){
		double result=0;
		for(int i=0; i<arr.length; i++){
			result+=arr[i]*Math.pow(input,i);
		}
		return result;
	}

	public boolean hasRoot(double input){
		return evaluate(input)==0;
	}
}