//Scrape All Data from Web site from 1985-2019

package Climate_Data;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NCDC {		
	
	
	public static List<WebElement> allStates = new ArrayList<WebElement>();
	public static List<String> States = new ArrayList<String>();
	public static List<WebElement> allCounties = new ArrayList<WebElement>();
	public static List<String> Counties = new ArrayList<String>();
	public static List<Integer> CountiesC = new ArrayList<Integer>();
	public static List<String> data = new ArrayList<String>();
	public static List<String> Climate = new ArrayList<String>();
	
	private static void createLists() throws Exception {
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");	
		WebDriver driver = new ChromeDriver();					
        		
        String baseUrl = "https://www.ncdc.noaa.gov/cag/county/time-series";					
        driver.get(baseUrl);
		
		
			
			WebElement parameter = driver.findElement(By.id("parameter"));
	        Select dropdown1 = new Select(parameter);							
	        dropdown1.selectByVisibleText("Precipitation");
			
	        WebElement timescale = driver.findElement(By.id("timescale"));
	        Select dropdown2 = new Select(timescale);							
	        dropdown2.selectByVisibleText("All Months");
	        
	        WebElement month = driver.findElement(By.id("month"));
	        Select dropdown3 = new Select(month);							
	        dropdown3.selectByVisibleText("April");
	        
	        WebElement begyear = driver.findElement(By.id("begyear"));
	        Select dropdown4 = new Select(begyear);							
	        dropdown4.selectByVisibleText("1895");
	        
	        WebElement endyear = driver.findElement(By.id("endyear"));
	        Select dropdown5 = new Select(endyear);							
	        dropdown5.selectByVisibleText("2019");
			
	        WebElement state = driver.findElement(By.id("state"));
	        Select dropdown6= new Select(state);							
	        allStates =  dropdown6.getOptions();
	        for(int j=0;j<allStates.size();j++) 
	        {
	        	String State = allStates.get(j).getText(); 
	        	States.add(State);
	        	dropdown6.selectByVisibleText(State);
	        	
	        	WebElement div = driver.findElement(By.id("div"));
	        	Select dropdown7= new Select(div);							
	        	allCounties =  dropdown7.getOptions();
	        	CountiesC.add(allCounties.size());
	        	for(int k=0;k<allCounties.size();k++) 
	        	{	
		        	System.out.println("Getting Links for "+(j+1)+" of "+allStates.size()+" States. "+(k+1)+" of "+allCounties.size()+" Counties");
	        		String County = allCounties.get(k).getText();
	        		Counties.add(County);
	        		//getData(type,State,County, i);
	        		
	        	}
		}
	        driver.close();
	        getData();
	}
		
	
	
	
	private static void getData() throws Exception {
		
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");	
		WebDriver driver = new ChromeDriver();		
        		
        String baseUrl = "https://www.ncdc.noaa.gov/cag/county/time-series";					
        driver.get(baseUrl);
        
        for(int i=4;i<5;i++) 
		{	
			String type="";
			switch(i) 
			{
			case 1: type="Average Temperature";
			break;
			case 2: type="Maximum Temperature";
			break;
			case 3: type="Minimum Temperature";
			break;
			case 4: type="Precipitation";
			break;
			}
			int cc=0;
			for(int j=25;j<States.size();j++) 
	        {
				cc=0;
				for(int c=0;c<j;c++) {cc+=CountiesC.get(c);}
				String State=States.get(j);
				
				for(int k=0;k<CountiesC.get(j);k++) {
					String County=Counties.get(cc);
					cc++;
					System.out.println((j+1)+" of "+allStates.size()+" States. "+(k+1)+" of "+CountiesC.get(j)+" Counties");
					
		WebElement parameter = driver.findElement(By.id("parameter"));
        Select dropdown1 = new Select(parameter);							
        dropdown1.selectByVisibleText(type);
		
        WebElement timescale = driver.findElement(By.id("timescale"));
        Select dropdown2 = new Select(timescale);							
        dropdown2.selectByVisibleText("All Months");
        
        WebElement month = driver.findElement(By.id("month"));
        Select dropdown3 = new Select(month);							
        dropdown3.selectByVisibleText("April");
        
        WebElement begyear = driver.findElement(By.id("begyear"));
        Select dropdown4 = new Select(begyear);							
        dropdown4.selectByVisibleText("1895");
        
        WebElement endyear = driver.findElement(By.id("endyear"));
        Select dropdown5 = new Select(endyear);							
        dropdown5.selectByVisibleText("2019");
		
        WebElement state = driver.findElement(By.id("state"));
        Select dropdown6= new Select(state);				
        dropdown6.selectByVisibleText(State);
		
        WebElement div = driver.findElement(By.id("div"));
        Select dropdown7= new Select(div);							
        dropdown7.selectByVisibleText(County);
        
        WebElement plot = driver.findElement(By.id("submit"));
        plot.click();
        
        String d=driver.findElement(By.xpath("//a[@id='anch_43']")).getAttribute("href");
        data.add(d);
        System.out.println(d);
        
        driver.get(d);
        String c = driver.findElement(By.tagName("body")).getText();
        Climate.add(c);
        //System.out.println(c);
        driver.get(baseUrl);
        write(State, County, c, i);
	
	        }
		}
	}
        driver.close();
	}
	
	private static void write(String State, String County, String z, int t) throws Exception {
		String fileName = "Climate_189501-201904//"+t+"//"+State+"_"+County+"_"+t+".txt";
		File outputFile=new File(fileName);
		outputFile.createNewFile();
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
			writer.write(z);
		writer.close();
		format(fileName,State,t);
	}
	 
	private static void format(String file, String State, int t) throws Exception {
		ArrayList<String> Exists = new ArrayList<String>();
		ArrayList<String> CSV = new ArrayList<String>();
		File outputFile=new File("Climate_189501-201904//State//"+State+"_"+t+".txt");
		
		if(outputFile.createNewFile()) {
			PrintWriter writer1 = new PrintWriter(outputFile, "UTF-8");
			writer1.write("County,State,Element,Date,Value,Anomaly");
			writer1.close();
		}
		Scanner sc0 = new Scanner(outputFile);
		while (sc0.hasNextLine()) {
			Exists.add(sc0.nextLine());
		}
		sc0.close();
		File inputFile=new File(file);
		Scanner sc = new Scanner(inputFile);
		while (sc.hasNextLine()) {
			CSV.add(sc.nextLine());
		}
		sc.close();
		
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		for(int i=0;i<Exists.size();i++) {
			writer.write(Exists.get(i));
			writer.println();
		}
			for(int i=5;i<CSV.size();i++) {
				String o = CSV.get(0)+","+CSV.get(i);
				writer.write(o);
				writer.println();
			}
		writer.close();
		
	}
	
	private static void writeData(List<String> s, String z) throws Exception {
		String fileName = "Output_"+z+".txt";
		File outputFile=new File(fileName);
		outputFile.createNewFile();
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		for(int i=0;i<s.size();i++) {
			writer.write(s.get(i));
			writer.println();
		}
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		createLists();
		//writeData(data,"Links");
		//writeData(Counties,"Counties");
		//writeData(States,"States");
		//writeData(Climate,"Climate");
	}
    		
    }		
