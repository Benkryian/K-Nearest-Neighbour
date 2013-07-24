
public class Vector_Class {

	float[] val;
	String classified;

	public Vector_Class(String str, float[] vars){
		classified = str;
		this.val = vars;
	}

	public float[] getValues(){
		return val;
	}

	public String getClassification(){
		return classified;
	}

	public void printToScreen(){
		System.out.println(classified +"\t"+ val[0]+"\t"+ val[1]+"\t"+ val[2]+"\t"+ val[3]);
	}
}
