//Akanksha Joshi - akankshj
package hw3;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;

public class ScoreController extends WordNerdController{
	
	//this is a placeholder class. Will be completed in HW3

	@Override
	void startController() {
		//to be implemented in HW3
		ScoreView scoreView = new ScoreView();
		ScoreChart scoreChart = new ScoreChart();
		scoreView.setupView();
		VBox lowerPanel = new VBox();
		lowerPanel.getChildren().add(scoreView.scoreGrid);				//add the scoreView grid to the panel
		wordNerdModel.readScore();
		List<LineChart<Number, Number>> lineChartList = new ArrayList<>();
		
		lineChartList=scoreChart.drawChart(wordNerdModel.scoreList);   //call the drawChart method for plotting the graphs based on contents of scorelist
		for(int i=0;i<lineChartList.size();i++) {
			scoreView.scoreGrid.add(lineChartList.get(i),0,i+1,2,1);   // add the charts at different rows in the grid
		}
		WordNerd.root.setPadding(new Insets(10, 10, 10, 10));
		WordNerd.root.setCenter(lowerPanel);
		
		
		
	}

	@Override
	void setupBindings() {
		//to be implemented in HW3
		
		
	}
	
	

}
