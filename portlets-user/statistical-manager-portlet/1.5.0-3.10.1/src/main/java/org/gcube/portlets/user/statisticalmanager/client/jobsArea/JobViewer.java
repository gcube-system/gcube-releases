/**
 * 
 */
package org.gcube.portlets.user.statisticalmanager.client.jobsArea;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.gcube.portlets.user.statisticalmanager.client.Constants;
import org.gcube.portlets.user.statisticalmanager.client.StatisticalManager;
import org.gcube.portlets.user.statisticalmanager.client.bean.JobItem;
import org.gcube.portlets.user.statisticalmanager.client.bean.Operator;
import org.gcube.portlets.user.statisticalmanager.client.widgets.ComputationOutputPanel;
import org.gcube.portlets.user.statisticalmanager.client.widgets.Section;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.event.WindowListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author ceras
 *
 */
public class JobViewer extends ContentPanel {
	private JobItem currentJobItem;


	public JobViewer() {
		super();
		this.setHeading(".: Computation Viewer");		
		this.setScrollMode(Scroll.AUTO);
	}


	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);
		this.getBody().setStyleAttribute("padding", "15px");
	}

	public void setEmpty() {
		this.removeAll();
		this.add(new Html("<br><center>No computation selected.</center>"));
		this.layout();
	}

	public void showJob(JobItem jobItem) {
		this.removeAll();
		
		addJobItemInformation(jobItem, this);

		this.currentJobItem = jobItem;
	}

	/**
	 * @param jobItem
	 * @param panel
	 */
	private static void addJobItemInformation(JobItem jobItem, ContentPanel panel) {
		Section section;
		Map<String, String> map;

		// title
		Html title = new Html(".: Computation Report of <b>"+jobItem.getName()+"</b>");
		title.addStyleName("jobViewer-title");
		panel.add(title);

		// output
		if (jobItem.getStatus().isComplete()) {
			section = new Section("Output Results", StatisticalManager.resources.tableResult());
//			section.setFrameHidden();
			section.add(new ComputationOutputPanel(jobItem.getId()));
			section.setPadding(15);
			panel.add(section);
		}

		// computation details
		map = new LinkedHashMap<String, String>();
		map.put("Name", jobItem.getName());
		map.put("Id", jobItem.getId());
		//map.put("Creation date", DateTimeFormat.getFullDateTimeFormat().format(jobItem.getCreationDate()));
		map.put("Creation date", DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(jobItem.getCreationDate()));
		
		
		if (jobItem.getEndDate() != null)
			//map.put("End date", DateTimeFormat.getFullDateTimeFormat().format(jobItem.getEndDate()));
			map.put("End date", DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(jobItem.getEndDate()));
		
		section = new Section("Computation Details", StatisticalManager.resources.details(), map);
		panel.add(section);

		addParametersSection(jobItem, panel);

		Operator operator = jobItem.getOperator();
		map = new HashMap<String, String>();
		map.put("Name", operator.getName());
		map.put("Category", operator.getCategory().getName());
		map.put("Description", operator.getBriefDescription());
		section = new Section("Operator Details", StatisticalManager.resources.details(), map);
		panel.add(section);

		panel.layout();
	}


	/**
	 * @return the currentJobItem
	 */
	public JobItem getCurrentJobItem() {
		return currentJobItem;
	}

	/**
	 * 
	 */
	private static void addParametersSection(JobItem jobItem, ContentPanel panel) {
		final LayoutContainer lc = new LayoutContainer();
		StatisticalManager.getService().getParametersMapByJobId(jobItem.getId(), new AsyncCallback<Map<String,String>>() {
			@Override
			public void onSuccess(Map<String, String> result) {
				lc.unmask();
				Section section = new Section("Parameters", StatisticalManager.resources.details(), result);
				lc.add(section);
				lc.setAutoHeight(true);
				lc.layout();
			}
			@Override
			public void onFailure(Throwable caught) {
				lc.unmask();
				MessageBox.alert("Error", "Impossible to load parameters values.", null);
			}
		});
		lc.mask("Load Parameters Values", Constants.maskLoadingStyle);
		panel.add(lc);
	}


	/**
	 * 
	 */
	public void showCurrentJobToWindow() {
		if (currentJobItem!=null) {
			final Window w = new Window() {
				@Override
				protected void onRender(Element parent, int pos) {
					super.onRender(parent, pos);
					this.getBody().setStyleAttribute("padding", "15px");
					this.getBody().setStyleAttribute("background-color", "#FFFFFF");
				}
			};
			w.setSize(700, 500);
			w.setClosable(true);
			w.setMaximizable(true);
			w.setResizable(true);
			w.setScrollMode(Scroll.AUTO);
			w.addWindowListener(new WindowListener() {
				@Override
				public void windowHide(WindowEvent we) {
					System.out.println("hide");
				}
			});
			addJobItemInformation(currentJobItem, w);
			w.show();
		}
	}

}

