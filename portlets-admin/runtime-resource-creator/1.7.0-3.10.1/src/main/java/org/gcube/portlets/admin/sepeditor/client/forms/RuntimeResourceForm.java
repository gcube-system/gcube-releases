package org.gcube.portlets.admin.sepeditor.client.forms;


import java.util.ArrayList;
import java.util.List;

import org.gcube.portlets.admin.sepeditor.client.RuntimeResourceCreator;
import org.gcube.portlets.admin.sepeditor.client.RuntimeResourceCreatorService;
import org.gcube.portlets.admin.sepeditor.client.RuntimeResourceCreatorServiceAsync;
import org.gcube.portlets.admin.sepeditor.shared.Category;
import org.gcube.portlets.admin.sepeditor.shared.ClientScope;
import org.gcube.portlets.admin.sepeditor.shared.FilledRuntimeResource;
import org.gcube.portlets.admin.sepeditor.shared.Property;
import org.gcube.portlets.admin.sepeditor.shared.RRAccessPoint;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Massimiliano Assante - ISTI-CNR
 * @version 1.1 Apr 10th 2012
 */
public class RuntimeResourceForm extends ContentPanel {

	protected RuntimeResourceCreatorServiceAsync runtimeService = (RuntimeResourceCreatorServiceAsync)GWT.create(RuntimeResourceCreatorService.class);
	RuntimeResourceCreator caller;
	private ArrayList<AccesPointPanel> accessPoints = new ArrayList<AccesPointPanel>();
	private FormData formData = null;
	private FormPanel form = null;
	private Button addAccessPointButton;
	private ArrayList<ClientScope> scopes;
	private FilledRuntimeResource toEdit;
	private boolean isEditMode = false;

	/**
	 * create mode 
	 * @param caller
	 * @param scopes
	 */
	public RuntimeResourceForm(RuntimeResourceCreator caller, ArrayList<ClientScope> scopes) {
		this.caller = caller;
		this.setHeaderVisible(true);
		this.setHeading("Service EndPoint Editor");
		//this.setModal(true);
		this.setAutoWidth(true);
		this.setHeight(650);
		//this.setResizable(true);
		this.setScrollMode(Scroll.AUTOY);
		this.getButtonBar().removeAll();
		this.scopes = scopes;
		
		formData = new FormData("-20");
		createForm();
	}
	/**
	 * edit mode
	 * @param caller
	 * @param scopes
	 * @param toEdit
	 */
	public RuntimeResourceForm(RuntimeResourceCreator caller, ArrayList<ClientScope> scopes, FilledRuntimeResource toEdit) {
		this.isEditMode = true;
		this.toEdit = toEdit;
		this.caller = caller;
		this.setHeaderVisible(true);
		this.setHeading("Service EndPoint Editor [EDIT MODE]");
		//this.setModal(true);
		this.setAutoWidth(true);
		this.setHeight(650);
		//this.setResizable(true);
		this.setScrollMode(Scroll.AUTOY);
		this.getButtonBar().removeAll();
		this.scopes = scopes;
		formData = new FormData("-20");
		createForm();
	}
	
	public final void closeDialog() {
		this.hide();
	}


	private void createForm() {
		form = new FormPanel();
		form.setFrame(true);
		form.setAutoWidth(true);

		form.setHeaderVisible(false);
		form.getHeader().setStyleName("x-hide-panel-header");


		//listbox scope

		ListStore<ClientScope> storeScope = new ListStore<ClientScope>();  
		storeScope.add(scopes);  

		ComboBox<ClientScope> comboScope = new ComboBox<ClientScope>();  
		comboScope.setFieldLabel("Scope");  
		comboScope.setDisplayField("name");  
		comboScope.setTriggerAction(TriggerAction.ALL);  
		comboScope.setStore(storeScope);  
		comboScope.setEditable(false);
		form.add(comboScope, formData);  

		TextField<String> resID = new TextField<String>();
		resID.setFieldLabel("Resource ID");
		// resID.setValidator(new StringValidator(120, false));
		resID.setAllowBlank(false);  // is required
		
		resID.setEmptyText(isEditMode ? toEdit.getResourceId() : "<generated by Resource Manager>");

		resID.setEnabled(false);
		form.add(resID, formData);

		TextField<String> resName = new TextField<String>();
		resName.setFieldLabel("Resource Name");
		// resName.setValidator(new StringValidator(120, false));
		resName.setAllowBlank(false);  // is required

		form.add(resName, formData);

		TextArea description = new TextArea();
		description.setFieldLabel("Resource Description");

		form.add(description, formData);

		TextField<String> resType = new TextField<String>();
		resType.setFieldLabel("Type");
		resType.setAllowBlank(false);  // is required
		// If in editing mode
		resType.setEmptyText("RuntimeResource");
		resType.setEnabled(false);
		form.add(resType, formData);

		//listbox
		List<Category> categories = getCategories();
		ListStore<Category> store = new ListStore<Category>();  
		store.add(categories);  

		ComboBox<Category> combo = new ComboBox<Category>();  
		combo.setFieldLabel("Category");  
		combo.setDisplayField("name");  
		combo.setTriggerAction(TriggerAction.ALL);  
		combo.setStore(store);  
		form.add(combo, formData);  

		//get the platform
		form.add(getPlatform());

		//get thr runtime
		form.add(getRuntime());

		String submitText = "Submit New";
		
		if (isEditMode) {
			comboScope.setValue(storeScope.getAt(0));
			resName.setValue(toEdit.getResourceName());
			description.setValue(toEdit.getDescription());
			combo.setValue(new Category(toEdit.getCategory()));
			
			for (RRAccessPoint ap : toEdit.getRRAccessPoints()) {
				addAccessPoint2Edit(ap);
			}
			submitText = "Submit Update";
		}
		
		//add access point
		addAccessPointButton = getAddAccessPointButton();
		form.add(addAccessPointButton);
		form.add(new Label()); //spacer

	
		
		Button submitBtn = new Button(submitText) {
			@Override
			protected void onClick(final ComponentEvent ce) {
				form.mask("sending encrypted info, please wait","loading-indicator");
				super.onClick(ce);
				if (!form.isValid()) {
					return;
				}
				String[] values = new String[form.getFields().size()];
				int i = 0;
				for (Field<?> field  : form.getFields()) {
					values[i] = field.getRawValue();
					GWT.log(i+ ": " + field.getRawValue()+"\n");
					i++;
				}

				FilledRuntimeResource rr = new FilledRuntimeResource();
				if (isEditMode)
					rr.setResourceId(toEdit.getResourceId());
				rr.setResourceName(values[2]);
				rr.setDescription(values[3]);
				rr.setCategory(values[5]);

				rr.setPlatformName(values[6]);
				rr.setPlatformVersion(values[7]);
				rr.setPlatformMinorVersion(values[8]);
				rr.setPlatformRevisionVersion(values[9]);
				rr.setPlatformBuildVersion(values[10]);

				rr.setRuntimeHostedOn(values[11]);
				rr.setRuntimeStatus(values[12]);
				rr.setRuntimegHNUniqueID(values[13]);	

				ArrayList<RRAccessPoint> rRAccessPoints = new ArrayList<RRAccessPoint>();
				for (AccesPointPanel ap : accessPoints) {
					RRAccessPoint rAP = new RRAccessPoint();
					rAP.setDesc(ap.getDesc().getRawValue());
					rAP.setInterfaceEndPoint(ap.getEndPoint().getRawValue());
					rAP.setInterfaceEntryNameAttr(ap.getEntryNameAttr().getRawValue());
					rAP.setUsername(ap.getUsername().getRawValue());
					rAP.setPassword(ap.getPassword().getRawValue());

					ArrayList<Property> properties = new ArrayList<Property>();
					for (PropertyPanel pp : ap.getProperties()) {
						Property toAdd = new Property();
						toAdd.setKey(pp.getKey().getRawValue());
						toAdd.setValue(pp.getValue().getRawValue());
						toAdd.setCrypted(pp.isCrypted());
						properties.add(toAdd);
					}
					rAP.setProperties(properties);
					rRAccessPoints.add(rAP);
				}
				rr.setRRAccessPoints(rRAccessPoints);				
				RuntimeResourceCreator.runtimeService.createRuntimeResource(values[0], rr, isEditMode, new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						form.unmask();
						if (result) {
							MessageBox.info("Service EndPoint Editor", "Request for registration successfully sent", null);	
							caller.initialize(null, null);
						}
						else {
							MessageBox.alert("Service EndPoint Editor", "We're sorry, Request for registration failed on server, please check server logs", null);	
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						form.unmask();
						MessageBox.alert("Service EndPoint Editor", "Request for registration Failed", null);	
						caller.initialize(null, null);
					}
				});
			}
		};
		form.addButton(submitBtn);
		form.setButtonAlign(HorizontalAlignment.CENTER);

		FormButtonBinding binding = new FormButtonBinding(form);
		binding.addButton(submitBtn);

		this.add(form);
	}

	private Button getAddAccessPointButton() {
		Button addAccessPoint = new Button("Add Access Point"){
			@Override
			protected void onClick(final ComponentEvent ce) {
				AccesPointPanel toAdd = getNewAccessPoint();
				addAccessPoint(toAdd);
				form.remove(addAccessPointButton);
				form.add(toAdd, formData);
				form.add(new Label());
				form.add(addAccessPointButton);
				form.layout();	
			}
		};
		return addAccessPoint;
	}

	/**
	 * 
	 * @return .
	 */
	private AccesPointPanel getNewAccessPoint() {
		return new AccesPointPanel(this, false, null);
	}
	
	/**
	 * 
	 * @return .
	 */
	private void addAccessPoint2Edit(RRAccessPoint source) {
		AccesPointPanel toAdd = new AccesPointPanel(this, source);
		addAccessPoint(toAdd);
		form.add(toAdd, formData);
		form.add(new Label());
		form.layout();	
	}

	private ArrayList<Category> getCategories() {
		final ArrayList<Category> defaultCats = new ArrayList<Category>();
		defaultCats.add(new Category("Application"));
		defaultCats.add(new Category("BiodiversityRepository"));
		defaultCats.add(new Category("Cloud"));
		defaultCats.add(new Category("Database"));
		defaultCats.add(new Category("DataStorage"));
		defaultCats.add(new Category("HostingPlatform"));
		defaultCats.add(new Category("FTPServer"));
		defaultCats.add(new Category("Gis"));
		defaultCats.add(new Category("MavenRepository"));
		defaultCats.add(new Category("OnlineService"));
		defaultCats.add(new Category("OpenSearchRepository"));
		defaultCats.add(new Category("Service"));
		return defaultCats;
	}

	private ArrayList<Category> getStatuses() {
		final ArrayList<Category> defaultCats = new ArrayList<Category>();
		defaultCats.add(new Category("READY"));
		defaultCats.add(new Category("UNAVAILABLE"));
		return defaultCats;
	}

	protected void addAccessPoint(AccesPointPanel toAdd) {
		this.accessPoints.add(toAdd);
	}

	protected void removeAccessPoint(AccesPointPanel toRemove) {
		this.accessPoints.remove(toRemove);
		form.remove(toRemove);
	}
	/**
	 * 
	 * @return .
	 */
	private FieldSet getPlatform() {
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setHeading("Platform"); 
		fieldSet.setCollapsible(true);

		FormLayout layout = new FormLayout();  
		layout.setLabelWidth(100);  
		fieldSet.setLayout(layout);  

		TextField<String> platformName = new TextField<String>();  
		platformName.setFieldLabel("Name");  
		platformName.setAllowBlank(false);  
		fieldSet.add(platformName, formData);  

		NumberField platformVersion = new NumberField();
		platformVersion.setFieldLabel("Version");  
		platformVersion.setPropertyEditorType(Integer.class);
		platformVersion.setAllowNegative(false);
		fieldSet.add(platformVersion, formData);  
		platformVersion.setAllowBlank(false);

		NumberField platformMinorVersion = new NumberField();
		platformMinorVersion.setFieldLabel("Minor Version");
		platformMinorVersion.setPropertyEditorType(Integer.class);
		platformMinorVersion.setAllowNegative(false);
		fieldSet.add(platformMinorVersion, formData);  
		platformMinorVersion.setAllowBlank(true);

		NumberField platformRevisionVersion = new NumberField();
		platformRevisionVersion.setFieldLabel("Revision Version"); 
		platformRevisionVersion.setPropertyEditorType(Integer.class);
		platformRevisionVersion.setAllowNegative(false);
		fieldSet.add(platformRevisionVersion, formData);  
		platformRevisionVersion.setAllowBlank(true);

		NumberField platformBuildVersion = new NumberField(); 
		platformBuildVersion.setFieldLabel("Build Version");
		platformBuildVersion.setPropertyEditorType(Integer.class);
		platformBuildVersion.setAllowNegative(false);
		fieldSet.add(platformBuildVersion, formData); 
		platformBuildVersion.setAllowBlank(true);
		
		if (isEditMode) {
			platformName.setValue(toEdit.getPlatformName());
			platformVersion.setValue((Number) Integer.parseInt(toEdit.getPlatformVersion()));
			platformMinorVersion.setValue((Number) Integer.parseInt(toEdit.getPlatformMinorVersion()));
			platformRevisionVersion.setValue((Number) Integer.parseInt(toEdit.getPlatformRevisionVersion()));
			platformBuildVersion.setValue((Number) Integer.parseInt(toEdit.getPlatformBuildVersion()));
		}		
		return fieldSet;  
	}

	/**
	 * 
	 * @return .
	 */
	private FieldSet getRuntime() {
		FieldSet fieldSet = new FieldSet();  
		fieldSet.setHeading("Runtime"); 
		fieldSet.setCollapsible(true);

		FormLayout layout = new FormLayout();  
		layout.setLabelWidth(100);  
		fieldSet.setLayout(layout);  

		TextField<String> hostedOn = new TextField<String>();  
		hostedOn.setFieldLabel("Hosted On");  
		hostedOn.setAllowBlank(false);  
		fieldSet.add(hostedOn, formData);  

		//listbox
		List<Category> statuses = getStatuses();
		ListStore<Category> store = new ListStore<Category>();  
		store.add(statuses);  

		ComboBox<Category> combo = new ComboBox<Category>();  
		combo.setFieldLabel("Status");  
		combo.setDisplayField("name");  
		combo.setTriggerAction(TriggerAction.ALL);  
		combo.setStore(store);  
		fieldSet.add(combo, formData);  

		TextField<String> gHNUniqueID = new TextField<String>();  
		gHNUniqueID.setFieldLabel("gHN UniqueID");  
		fieldSet.add(gHNUniqueID, formData);  
		gHNUniqueID.setAllowBlank(true);

		if (isEditMode) {
			hostedOn.setValue(toEdit.getRuntimeHostedOn());
			combo.setValue(new Category(toEdit.getRuntimeStatus()));
			gHNUniqueID.setValue(toEdit.getRuntimegHNUniqueID());
		}
		
		return fieldSet;  
	}
}
