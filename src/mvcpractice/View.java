/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvcpractice;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Observable;
import java.util.Observer;
import mvcpractice.Model.TemperatureModel;

/**
 *
 * @author Taharih Rogers
 */
public class View {
    
    abstract static class TemperatureGUI implements java.util.Observer
    {	TemperatureGUI(String label, TemperatureModel model, int h, int v)
	{	this.label = label;
		this.model = model;
		temperatureFrame = new Frame(label);
		temperatureFrame.add("North", new Label(label));
		temperatureFrame.add("Center", display);
		Panel buttons = new Panel();
		buttons.add(upButton);
		buttons.add(downButton);		
		temperatureFrame.add("South", buttons);		
		temperatureFrame.addWindowListener(new CloseListener());	
		model.addObserver(this); // Connect the View to the Model
		temperatureFrame.setSize(200,100);
		temperatureFrame.setLocation(h, v);
		temperatureFrame.setVisible(true);
	}
	
	public void setDisplay(String s){ display.setText(s);}
	
	public double getDisplay()
	{	double result = 0.0;
		try
		{	result = Double.valueOf(display.getText()).doubleValue();
		}
		catch (NumberFormatException e){}
		return result;
	}
	
	public void addDisplayListener(ActionListener a){ display.addActionListener(a);}
	public void addUpListener(ActionListener a){ upButton.addActionListener(a);}
	public void addDownListener(ActionListener a){ downButton.addActionListener(a);}
	
	protected TemperatureModel model(){return model;}
	
	private String label;
	private TemperatureModel model;
	private Frame temperatureFrame;
	private TextField display = new TextField();
	private Button upButton = new Button("Raise");
	private Button downButton = new Button("Lower");

        private WindowListener CloseListener() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
	
	public class CloseListener extends WindowAdapter
	{	public void windowClosing(WindowEvent e)
		{	e.getWindow().setVisible(false);
			System.exit(0);
		}
	}
    }//end of TemperatureGUI
    
    static class FarenheitGUI extends TemperatureGUI
    {	public FarenheitGUI(TemperatureModel model, int h, int v)
	{	super("Farenheit Temperature", model, h, v);
		setDisplay(""+model.getF());
		addUpListener(new UpListener());
		addDownListener(new DownListener());
		addDisplayListener(new DisplayListener());
	}
	
	public void update(Observable t, Object o) // Called from the Model
	{	setDisplay("" + model().getF());
	}
	
	class UpListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	model().setF(model().getF() + 1.0);
		}
	}
	
	class DownListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	model().setF(model().getF() - 1.0);
		}
	}
	
	class DisplayListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	double value = getDisplay();
			model().setF(value);
		}
	}
    }//end of farenheitgui
    
    static class CelsiusGUI extends TemperatureGUI
    {	public CelsiusGUI(TemperatureModel model, int h, int v)
	{	super("Celsius Temperature", model, h, v);
		setDisplay(""+model.getC());
		addUpListener(new UpListener());
		addDownListener(new DownListener());
		addDisplayListener(new DisplayListener());
	}
	
	public void update(Observable t, Object o) // Called from the Model
	{	setDisplay("" + model().getC());
	}
	
	class UpListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	model().setC(model().getC() + 1.0);
		}
	}
	
	class DownListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	model().setC(model().getC() - 1.0);
		}
	}
	
	class DisplayListener implements ActionListener
	{	public void actionPerformed(ActionEvent e)
		{	double value = getDisplay();
			model().setC(value);
		}
	}
    }//end of celsius gui
    
     //public static class MVCTempConvert 
     //{   
          //public static void main(String args[]) 
        //{       TemperatureModel temperature = new TemperatureModel();
             //   new FarenheitGUI(temperature, 100, 100);
           //     new CelsiusGUI(temperature,100, 250);
       // }
   // }//end of mvcTempConvert
     
     static class TemperatureCanvas extends Canvas
    {	public TemperatureCanvas(TemperatureGauge farenheit)
	{	_farenheit = farenheit;
	}
	
	public void paint(Graphics g)
	{	g.setColor(Color.black);
		g.drawRect(left,top, width, height);
		g.setColor(Color.red);
		g.fillOval(left-width/2, top+height-width/3,width*2, width*2);
		g.setColor(Color.black);
		g.drawOval(left-width/2, top+height-width/3,width*2, width*2);
		g.setColor(Color.white);
		g.fillRect(left+1,top+1, width-1, height-1);
		g.setColor(Color.red);
		long redtop = height*(_farenheit.get()-_farenheit.getMax())/(_farenheit.getMin()-_farenheit.getMax());
		g.fillRect(left+1, top + (int)redtop, width-1, height-(int)redtop);
	}
	
	private TemperatureGauge _farenheit;
	private static final int width = 20;
	private static final int top = 20;
	private static final int left = 100;
	private static final int right = 250;
	private static final int height = 200;
    }//end of TemperatureCanvas

    static class TemperatureGauge
    {	public TemperatureGauge(int min, int max){ Min = min; Max = max; }
	
	public void set(int level) { current = level; }	
	public int get(){return current;}
	public int getMax(){return Max;}
	public int getMin(){return Min;}
	
	private int Max, Min, current;
    }//end of temperatureguage

    public static class GraphGUI extends Frame implements Observer
    {	public GraphGUI(TemperatureModel model, int h, int v)
	{ 	super("Temperature Gauge");
		this.model = model;
		_farenheit = new TemperatureGauge(-200, 300);
		Panel Top = new Panel();
		add("North", Top);
		gauges = new TemperatureCanvas(_farenheit);
		gauges.setSize(500,280);
		add("Center", gauges);		setSize(280, 280);
		setLocation(h,v);
		setVisible(true);
		model.addObserver(this); // Connect to the model
	}
	
	public void update(Observable obs, Object o) // Respond to changes
	{	repaint();
	}
		
	public void paint(Graphics g)
	{	int farenheit = (int)model.getF(); // Use the current data to paint
		_farenheit.set(farenheit);
		gauges.repaint();
		super.paint(g);
	}
	
	private TemperatureModel model;
	private Canvas gauges;
	private TemperatureGauge _farenheit;
}//end of graph gui
    
    public static class SliderGUI implements Observer
{	public SliderGUI(TemperatureModel m, int h, int v)
	{	m.addObserver(this); //Observe the temperature model
		model = m;
		sliderFrame.add(tempControl);
		tempControl.addAdjustmentListener(new SlideListener());
		sliderFrame.setSize(250,50);
		sliderFrame.setLocation(h, v);
		sliderFrame.setVisible(true);
		//sliderFrame.addWindowListener(new TemperatureGUI.CloseListener());		
	}
	
	public void update(Observable t, Object o)
	{	double temp = ((TemperatureModel)t).getC();
		tempControl.setValue((int)temp); // Move the slider thumb
	}
	
	class SlideListener implements AdjustmentListener
	{	public void adjustmentValueChanged(AdjustmentEvent e)
		{	model.setC(tempControl.getValue());
		}
	}
	
	private Scrollbar tempControl = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, -50, 160);
	private TemperatureModel model = null;
	private Frame sliderFrame = new Frame("Celsius");
}//end of sliderGUI
    
    public static class MVCTempConvert 
     {   
          public static void main(String args[]) 
        {       TemperatureModel temperature = new TemperatureModel();
                new FarenheitGUI(temperature, 100, 100);
                new CelsiusGUI(temperature,100, 250);
                TemperatureGauge gauge = new TemperatureGauge(100, 500);
                new TemperatureCanvas(gauge);
                new GraphGUI(temperature, 100, 500);
                new SliderGUI(temperature, 100, 500);
        }
    }//end of mvcTempConvert
    
}//end of view
