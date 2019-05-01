import java.awt.Color;


import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import acm.gui.TableLayout;

public class sliderBox {

JPanel myPanel;
JLabel nameLabel;
JLabel minLabel;
JLabel maxLabel;
JSlider mySlider;
JLabel sReadout;
Integer imin;
Integer imax;

public sliderBox(String name, double min, double dValue,
double max) { // Integer values
myPanel = new JPanel();
nameLabel = new JLabel(name);
minLabel = new JLabel(min+"");
maxLabel = new JLabel(max+"");
mySlider = new JSlider((int)(min),(int)(max),(int)(dValue));
sReadout = new JLabel(dValue+"");
sReadout.setForeground(Color.blue);
myPanel.setLayout(new TableLayout(1,5));
myPanel.add(nameLabel,"width=100");
myPanel.add(minLabel,"width=25");
myPanel.add(mySlider,"width=100");
myPanel.add(maxLabel,"width=100");
myPanel.add(sReadout,"width=80");
imin=(int)min;
imax=(int)max;
}

public Integer getISlider() {
return mySlider.getValue();
}
public void setISlider(int val) {
mySlider.setValue(val);
sReadout.setText(val+"");
 
}
}