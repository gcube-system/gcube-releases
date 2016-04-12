package org.gcube.portlets.user.td.tablewidget.client.normalize;

import java.util.ArrayList;

import org.gcube.portlets.user.td.expressionwidget.client.properties.ColumnDataPropertiesCombo;
import org.gcube.portlets.user.td.gwtservice.client.rpc.TDGWTServiceAsync;
import org.gcube.portlets.user.td.gwtservice.shared.exception.TDGWTIsFinalException;
import org.gcube.portlets.user.td.gwtservice.shared.exception.TDGWTIsLockedException;
import org.gcube.portlets.user.td.gwtservice.shared.exception.TDGWTSessionExpiredException;
import org.gcube.portlets.user.td.gwtservice.shared.tr.normalization.DenormalizationSession;
import org.gcube.portlets.user.td.monitorwidget.client.MonitorDialog;
import org.gcube.portlets.user.td.monitorwidget.client.MonitorDialogListener;
import org.gcube.portlets.user.td.tablewidget.client.resources.ResourceBundle;
import org.gcube.portlets.user.td.tablewidget.client.util.UtilsGXT3;
import org.gcube.portlets.user.td.widgetcommonevent.client.event.ChangeTableRequestEvent;
import org.gcube.portlets.user.td.widgetcommonevent.client.event.SessionExpiredEvent;
import org.gcube.portlets.user.td.widgetcommonevent.client.type.ChangeTableRequestType;
import org.gcube.portlets.user.td.widgetcommonevent.client.type.ChangeTableWhy;
import org.gcube.portlets.user.td.widgetcommonevent.client.type.SessionExpiredType;
import org.gcube.portlets.user.td.widgetcommonevent.shared.OperationResult;
import org.gcube.portlets.user.td.widgetcommonevent.shared.TRId;
import org.gcube.portlets.user.td.widgetcommonevent.shared.tr.column.ColumnData;
import org.gcube.portlets.user.td.widgetcommonevent.shared.tr.column.ColumnDataType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadConfigBean;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * 
 * @author giancarlo
 * email: <a href="mailto:g.panichi@isti.cnr.it">g.panichi@isti.cnr.it</a> 
 *
 */
public class DenormalizePanel extends FramedPanel implements
		MonitorDialogListener {
	protected String WIDTH = "640px";
	protected String HEIGHT = "520px";

	protected TRId trId;
	protected EventBus eventBus;
	protected ArrayList<String> rows;

	protected DenormalizationSession denormalizationSession;

	protected ComboBox<ColumnData> comboValueColumn = null;
	protected ComboBox<ColumnData> comboAttributeColumn = null;
	protected ListStore<ColumnData> storeComboAttributeColumn;

	protected ListLoader<ListLoadConfig, ListLoadResult<ColumnData>> loader;

	protected FieldLabel comboAttributeColumnLabel;

	protected TextButton denormalizeButton;

	protected DenormalizeDialog parent;

	protected ArrayList<ColumnData> columnsRetrieved;

	public DenormalizePanel(DenormalizeDialog parent, TRId trId,
			EventBus eventBus) {
		this.parent = parent;
		this.trId = trId;
		this.eventBus = eventBus;
		create();
	}

	public DenormalizePanel(TRId trId, EventBus eventBus) {
		this.trId = trId;
		this.eventBus = eventBus;
		this.parent = null;
		create();
	}

	protected void create() {

		setWidth(WIDTH);
		setHeight(HEIGHT);
		setHeaderVisible(false);
		setBodyBorder(false);
		Log.debug("Create DenormalizationPanel(): [" + trId.toString() + "]");

		ColumnDataPropertiesCombo propsColumnData = GWT
				.create(ColumnDataPropertiesCombo.class);

		// Combo Value Column
		ListStore<ColumnData> storeComboValueColumn = new ListStore<ColumnData>(
				propsColumnData.id());

		Log.trace("StoreComboValueColumn created");

		RpcProxy<ListLoadConfig, ListLoadResult<ColumnData>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<ColumnData>>() {

			public void load(ListLoadConfig loadConfig,
					final AsyncCallback<ListLoadResult<ColumnData>> callback) {
				loadData(loadConfig, callback);
			}
		};

		loader = new ListLoader<ListLoadConfig, ListLoadResult<ColumnData>>(
				proxy) {
			@Override
			protected ListLoadConfig newLoadConfig() {
				return (ListLoadConfig) new ListLoadConfigBean();
			}

		};

		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ColumnData, ListLoadResult<ColumnData>>(
				storeComboValueColumn));
		Log.trace("LoaderCombo created");

		comboValueColumn = new ComboBox<ColumnData>(storeComboValueColumn,
				propsColumnData.label()) {

			protected void onAfterFirstAttach() {
				super.onAfterFirstAttach();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						loader.load();
					}
				});
			}
		};
		Log.trace("Combo Value Column created");

		addHandlersForComboColumn(propsColumnData.label());

		comboValueColumn.setLoader(loader);
		comboValueColumn.setEmptyText("Select a column...");
		comboValueColumn.setWidth(191);
		comboValueColumn.setTypeAhead(false);
		comboValueColumn.setEditable(false);
		comboValueColumn.setTriggerAction(TriggerAction.ALL);

		// Combo Attribute Column
		storeComboAttributeColumn = new ListStore<ColumnData>(
				propsColumnData.id());
		Log.trace("StoreComboAttributeColumn created");

		// storeComboAttributeColumn.addAll(ColumnDataTypeStore.getAttributeType());

		comboAttributeColumn = new ComboBox<ColumnData>(
				storeComboAttributeColumn, propsColumnData.label());
		Log.trace("ComboAttributeColumn created");

		addHandlersForComboAttributeColumn(propsColumnData.label());

		comboAttributeColumn.setEmptyText("Select a column...");
		comboAttributeColumn.setWidth(191);
		comboAttributeColumn.setTypeAhead(true);
		comboAttributeColumn.setTriggerAction(TriggerAction.ALL);

		comboAttributeColumnLabel = new FieldLabel(comboAttributeColumn,
				"Attribute Column");

		// Normalize Button
		denormalizeButton = new TextButton("Denormalize");
		denormalizeButton.setIcon(ResourceBundle.INSTANCE.tableDenormalize());
		denormalizeButton.setIconAlign(IconAlign.RIGHT);
		denormalizeButton.setTitle("Denormalize");

		SelectHandler deleteHandler = new SelectHandler() {

			public void onSelect(SelectEvent event) {
				onDenormalize();

			}
		};
		denormalizeButton.addSelectHandler(deleteHandler);

		HBoxLayoutContainer hBox = new HBoxLayoutContainer();
		hBox.add(denormalizeButton, new BoxLayoutData(new Margins(2, 5, 2, 5)));

		VerticalLayoutContainer v = new VerticalLayoutContainer();
		//v.setScrollMode(ScrollMode.AUTO); Set in GXT 3.0.1
		v.add(new FieldLabel(comboValueColumn, "Value Column"),
				new VerticalLayoutData(1, -1, new Margins(1)));

		v.add(comboAttributeColumnLabel, new VerticalLayoutData(1, -1,
				new Margins(1)));

		v.add(hBox, new VerticalLayoutData(-1, -1, new Margins(10, 0, 10, 0)));
		add(v, new VerticalLayoutData(-1, -1, new Margins(0)));

		comboAttributeColumnLabel.setVisible(false);
		denormalizeButton.disable();

	}

	protected void addHandlersForComboColumn(
			final LabelProvider<ColumnData> labelProvider) {
		comboValueColumn
				.addSelectionHandler(new SelectionHandler<ColumnData>() {
					public void onSelection(SelectionEvent<ColumnData> event) {
						Info.display(
								"Value Column Selected",
								"You selected "
										+ (event.getSelectedItem() == null ? "nothing"
												: labelProvider.getLabel(event
														.getSelectedItem())
														+ "!"));
						Log.debug("ComboValueColumn selected: "
								+ event.getSelectedItem());
						ColumnData valueColumn = event.getSelectedItem();
						updateComboValueColumn(valueColumn);
					}

				});
	}

	protected void addHandlersForComboAttributeColumn(
			final LabelProvider<ColumnData> labelProvider) {
		comboAttributeColumn
				.addSelectionHandler(new SelectionHandler<ColumnData>() {
					public void onSelection(SelectionEvent<ColumnData> event) {
						Info.display(
								"Attribute Column Selected",
								"You selected "
										+ (event.getSelectedItem() == null ? "nothing"
												: labelProvider.getLabel(event
														.getSelectedItem())
														+ "!"));
						Log.debug("ComboAttributeColumn selected: "
								+ event.getSelectedItem());
						ColumnData attributeColumn = event.getSelectedItem();
						updateComboAttributeColumn(attributeColumn);
					}

				});
	}

	protected void updateComboValueColumn(ColumnData columnData) {
		comboAttributeColumn.clear();
		storeComboAttributeColumn.clear();
		for (ColumnData c : columnsRetrieved) {
			if (c.getColumnId().compareTo(columnData.getColumnId()) != 0) {
				storeComboAttributeColumn.add(c);
			}
		}
		storeComboAttributeColumn.commitChanges();
		comboAttributeColumnLabel.setVisible(true);
		denormalizeButton.disable();
		forceLayout();

	}

	protected void updateComboAttributeColumn(ColumnData columnData) {
		denormalizeButton.enable();
		forceLayout();
	}

	protected void resetComboStatus() {
		comboAttributeColumn.clear();
		storeComboAttributeColumn.clear();
		storeComboAttributeColumn.commitChanges();
		comboAttributeColumnLabel.setVisible(false);
		denormalizeButton.disable();
		forceLayout();
	}

	protected void loadData(ListLoadConfig loadConfig,
			final AsyncCallback<ListLoadResult<ColumnData>> callback) {
		TDGWTServiceAsync.INSTANCE.getColumns(trId,
				new AsyncCallback<ArrayList<ColumnData>>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof TDGWTSessionExpiredException) {
							eventBus.fireEvent(new SessionExpiredEvent(
									SessionExpiredType.EXPIREDONSERVER));
						} else {
							if (caught instanceof TDGWTIsLockedException) {
								Log.error(caught.getLocalizedMessage());
								UtilsGXT3.alert("Error Locked",
										caught.getLocalizedMessage());
							} else {
								Log.error("load combo failure:"
										+ caught.getLocalizedMessage());
								UtilsGXT3.alert("Error",
										"Error retrieving columns of tabular resource:"
												+ trId.getId());
							}
						}
						callback.onFailure(caught);
					}

					public void onSuccess(ArrayList<ColumnData> result) {
						Log.trace("loaded " + result.size() + " ColumnData");
						columnsRetrieved = result;
						resetComboStatus();
						ArrayList<ColumnData> columnsIntegerNumeric = new ArrayList<ColumnData>();
						for (ColumnData c : result) {
							if (c.getDataTypeName().compareTo(
									ColumnDataType.Numeric.toString()) == 0
									|| c.getDataTypeName().compareTo(
											ColumnDataType.Integer.toString()) == 0) {
								columnsIntegerNumeric.add(c);
							}
						}
						if (columnsIntegerNumeric.size() < 1) {
							UtilsGXT3
									.alert("Attention",
											"No Column with data type Integer or Numeric");
							return;
						}

						callback.onSuccess(new ListLoadResultBean<ColumnData>(
								columnsIntegerNumeric));

					}

				});

	}

	public void update(TRId trId) {
		this.trId = trId;
		loader.load();

	}

	protected void onDenormalize() {

		ColumnData valueColumn = comboValueColumn.getCurrentValue();
		if (valueColumn == null) {
			UtilsGXT3.alert("Attention", "Attention no value column selected!");
			return;
		}

		ColumnData attributeColumn = comboAttributeColumn.getCurrentValue();
		if (attributeColumn == null) {
			UtilsGXT3.alert("Attention",
					"Attention no attribute column selected!");
			return;
		}

		denormalizationSession = new DenormalizationSession(trId, valueColumn,
				attributeColumn);

		TDGWTServiceAsync.INSTANCE.startDenormalization(denormalizationSession,
				new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						if (caught instanceof TDGWTSessionExpiredException) {
							eventBus.fireEvent(new SessionExpiredEvent(
									SessionExpiredType.EXPIREDONSERVER));
						} else {
							if (caught instanceof TDGWTIsLockedException) {
								Log.error(caught.getLocalizedMessage());
								UtilsGXT3.alert("Error Locked",
										caught.getLocalizedMessage());
							} else {
								if (caught instanceof TDGWTIsFinalException) {
									Log.error(caught.getLocalizedMessage());
									UtilsGXT3.alert("Error Final",
											caught.getLocalizedMessage());
								} else {
									Log.debug("Denormalize Error: "
											+ caught.getLocalizedMessage());
									UtilsGXT3.alert("Error on Denormalize",
											caught.getLocalizedMessage());
								}
							}
						}
					}

					public void onSuccess(String taskId) {
						openMonitorDialog(taskId);
					}

				});

	}

	public void close() {
		if (parent != null) {
			parent.close();
		}
	}

	// /
	protected void openMonitorDialog(String taskId) {
		MonitorDialog monitorDialog = new MonitorDialog(taskId, eventBus);
		monitorDialog.addProgressDialogListener(this);
		monitorDialog.show();
	}

	@Override
	public void operationComplete(OperationResult operationResult) {
		ChangeTableWhy why = ChangeTableWhy.TABLEUPDATED;
		ChangeTableRequestEvent changeTableRequestEvent = new ChangeTableRequestEvent(
				ChangeTableRequestType.DENORMALIZE, operationResult.getTrId(), why);
		eventBus.fireEvent(changeTableRequestEvent);
		close();
	}

	@Override
	public void operationFailed(Throwable caught, String reason, String details) {
		UtilsGXT3.alert(reason, details);
		close();

	}

	@Override
	public void operationStopped(OperationResult operationResult, String reason, String details) {
		ChangeTableWhy why = ChangeTableWhy.TABLECURATION;
		ChangeTableRequestEvent changeTableRequestEvent = new ChangeTableRequestEvent(
				ChangeTableRequestType.DENORMALIZE, operationResult.getTrId(), why);
		eventBus.fireEvent(changeTableRequestEvent);
		close();

	}

	@Override
	public void operationAborted() {
		close();

	}

	@Override
	public void operationPutInBackground() {
		close();

	}

}
