package org.gcube.portlets.user.statisticalalgorithmsimporter.client.tools.explorer;

import org.gcube.portlets.user.statisticalalgorithmsimporter.client.event.DeleteItemEvent;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.event.MainCodeSetEvent;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.event.ProjectStatusEvent;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.event.SessionExpiredEvent;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.resource.StatAlgoImporterResources;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.rpc.StatAlgoImporterServiceAsync;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.type.SessionExpiredType;
import org.gcube.portlets.user.statisticalalgorithmsimporter.client.utils.UtilsGXT3;
import org.gcube.portlets.user.statisticalalgorithmsimporter.shared.exception.StatAlgoImporterSessionExpiredException;
import org.gcube.portlets.user.statisticalalgorithmsimporter.shared.workspace.ItemDescription;
import org.gcube.portlets.widgets.workspaceuploader.client.WorkspaceUploadNotification.WorskpaceUploadNotificationListener;
import org.gcube.portlets.widgets.workspaceuploader.client.uploader.DialogUpload.UPLOAD_TYPE;
import org.gcube.portlets.widgets.workspaceuploader.client.uploader.dragdrop.MultipleDNDUpload;
import org.gcube.portlets.widgets.wsexplorer.client.explore.WorkspaceResourcesExplorerPanel;
import org.gcube.portlets.widgets.wsexplorer.client.notification.WorkspaceExplorerSelectNotification.WorskpaceExplorerSelectNotificationListener;
import org.gcube.portlets.widgets.wsexplorer.shared.Item;
import org.gcube.portlets.widgets.wsexplorer.shared.ItemType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * 
 * @author giancarlo email: <a
 *         href="mailto:g.panichi@isti.cnr.it">g.panichi@isti.cnr.it</a>
 *
 */
public class ExplorerProjectPanel extends ContentPanel {

	private EventBus eventBus;
	private Item selectedItem;
	private MultipleDNDUpload dnd;
	private WorkspaceResourcesExplorerPanel wsResourcesExplorerPanel;
	private TextButton btnSetMain;
	private TextButton btnOpen;
	private TextButton btnDelete;
	private TextButton btnReload;

	public ExplorerProjectPanel(EventBus eventBus) {
		super();
		Log.debug("ExplorerProjectPanel");
		this.eventBus = eventBus;

		// msgs = GWT.create(ServiceCategoryMessages.class);
		init();
		bindToEvents();

	}

	public ExplorerProjectPanel(EventBus eventBus,
			AccordionLayoutAppearance appearance) {
		super(appearance);
		Log.debug("ExplorerProjectPanel");
		this.eventBus = eventBus;

		// msgs = GWT.create(ServiceCategoryMessages.class);
		init();
		bindToEvents();

	}

	private void init() {
		setId("ExplorerProjectPanel");
		forceLayoutOnResize = true;
		setBodyBorder(true);
		setBorders(true);
		setHeaderVisible(true);
		setResize(true);
		setAnimCollapse(false);
		setHeadingText("Project Explorer");
		

	}

	private void bindToEvents() {

		eventBus.addHandler(ProjectStatusEvent.TYPE,
				new ProjectStatusEvent.ProjectStatusEventHandler() {

					@Override
					public void onProjectStatus(ProjectStatusEvent event) {
						manageProjectStatusEvents(event);

					}
				});
	}

	private void manageProjectStatusEvents(ProjectStatusEvent event) {
		Log.debug("InputVariablePanel recieved event ProjectStatus: "
				+ event.toString());
		switch (event.getProjectStatusEventType()) {
		case START:
			break;
		case OPEN:
		case UPDATE:
		case ADD_RESOURCE:
		case DELETE_RESOURCE:
		case DELETE_MAIN_CODE:	
			create(event);
			break;
		case SAVE:	
		case MAIN_CODE_SET:
		case SOFTWARE_CREATED:
		case SOFTWARE_PUBLISH:
		case SOFTWARE_REPACKAGE:
		case EXPLORER_REFRESH:	
			reloadWSResourceExplorerPanel();
			break;
		default:
			break;
		}
	}

	private void create(ProjectStatusEvent event) {
		try {

			wsResourcesExplorerPanel = new WorkspaceResourcesExplorerPanel(
					event.getProject().getProjectFolder().getFolder()
							.getId(), false);
			
			
			
			
			WorskpaceExplorerSelectNotificationListener wsResourceExplorerListener = new WorskpaceExplorerSelectNotificationListener() {
				@Override
				public void onSelectedItem(Item item) {
					Log.debug("Listener Selected Item " + item);
					selectedItem = item;

				}

				@Override
				public void onFailed(Throwable throwable) {
					Log.error(throwable.getLocalizedMessage());
					throwable.printStackTrace();
				}

				@Override
				public void onAborted() {

				}

				@Override
				public void onNotValidSelection() {
					selectedItem = null;
				}
			};

			wsResourcesExplorerPanel
					.addWorkspaceExplorerSelectNotificationListener(wsResourceExplorerListener);
			wsResourcesExplorerPanel.ensureDebugId("wsResourceExplorerPanel");

			VerticalLayoutContainer vResourcesExplorerContainer = new VerticalLayoutContainer();
			vResourcesExplorerContainer.setScrollMode(ScrollMode.AUTO);
			vResourcesExplorerContainer.add(wsResourcesExplorerPanel,
					new VerticalLayoutData(1, -1, new Margins(0)));

			// DND

			dnd = new MultipleDNDUpload();
			dnd.setParameters(event.getProject().getProjectFolder()
					.getFolder().getId(), UPLOAD_TYPE.File);
			dnd.addUniqueContainer(vResourcesExplorerContainer);
			WorskpaceUploadNotificationListener workspaceUploaderListener = new WorskpaceUploadNotificationListener() {

				@Override
				public void onUploadCompleted(String parentId, String itemId) {
					Log.debug("Upload completed: [parentID: " + parentId
							+ ", itemId: " + itemId + "]");
					wsResourcesExplorerPanel.refreshRootFolderView();
					forceLayout();

				}

				@Override
				public void onUploadAborted(String parentId, String itemId) {
					Log.debug("Upload Aborted: [parentID: " + parentId
							+ ", itemId: " + itemId + "]");
				}

				@Override
				public void onError(String parentId, String itemId,
						Throwable throwable) {
					Log.debug("Upload Error: [parentID: " + parentId
							+ ", itemId: " + itemId + "]");
					throwable.printStackTrace();
				}

				@Override
				public void onOverwriteCompleted(String parentId, String itemId) {
					Log.debug("Upload Override Completed: [parentID: "
							+ parentId + ", itemId: " + itemId + "]");
					wsResourcesExplorerPanel.refreshRootFolderView();
					forceLayout();
				}
			};

			dnd.addWorkspaceUploadNotificationListener(workspaceUploaderListener);

			// ToolBar
			btnSetMain = new TextButton("Set Main");
			btnSetMain.setIcon(StatAlgoImporterResources.INSTANCE.add16());
			btnSetMain.setScale(ButtonScale.SMALL);
			btnSetMain.setIconAlign(IconAlign.LEFT);
			btnSetMain.setToolTip("Set main code");
			btnSetMain.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					setMainCode(event);
				}

			});

			btnOpen = new TextButton("Open");
			btnOpen.setIcon(StatAlgoImporterResources.INSTANCE.download16());
			btnOpen.setScale(ButtonScale.SMALL);
			btnOpen.setIconAlign(IconAlign.LEFT);
			btnOpen.setToolTip("Open");
			btnOpen.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					openFile();
				}

			});

			btnDelete = new TextButton("Delete");
			btnDelete.setIcon(StatAlgoImporterResources.INSTANCE.delete16());
			btnDelete.setScale(ButtonScale.SMALL);
			btnDelete.setIconAlign(IconAlign.LEFT);
			btnDelete.setToolTip("Delete");
			btnDelete.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					deleteItem(event);
				}

			});

			btnReload = new TextButton("Reload");
			btnReload.setIcon(StatAlgoImporterResources.INSTANCE.reload16());
			btnReload.setScale(ButtonScale.SMALL);
			btnReload.setIconAlign(IconAlign.LEFT);
			btnReload.setToolTip("Reload");
			btnReload.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					reloadWSResourceExplorerPanel();
				}

			});

			ToolBar toolBar = new ToolBar();
			toolBar.add(btnSetMain, new BoxLayoutData(new Margins(0)));
			toolBar.add(btnOpen, new BoxLayoutData(new Margins(0)));
			toolBar.add(btnDelete, new BoxLayoutData(new Margins(0)));
			toolBar.add(btnReload, new BoxLayoutData(new Margins(0)));
			
			VerticalLayoutContainer v = new VerticalLayoutContainer();

			v.add(toolBar, new VerticalLayoutData(1, -1, new Margins(0)));
			v.add(dnd, new VerticalLayoutData(1, 1, new Margins(0)));
			add(v, new MarginData(new Margins(0)));
			forceLayout();

		} catch (Exception e) {
			Log.error("Error opening wsResourceExplorerPanel");
			e.printStackTrace();
		}
	}

	protected void reloadWSResourceExplorerPanel() {
		if (wsResourcesExplorerPanel != null) {
			wsResourcesExplorerPanel.refreshRootFolderView();
		}

	}

	private void setMainCode(SelectEvent event) {
		Log.debug("Set Main Code");
		if (selectedItem != null
				&& selectedItem.getType().compareTo(ItemType.EXTERNAL_FILE) == 0) {
			loadData();
		} else {
			UtilsGXT3.info("Attention",
					"Select a valid file to be used as main!");
		}
	}

	private void deleteItem(SelectEvent event) {
		ItemDescription itemDescription = new ItemDescription(
				selectedItem.getId(), selectedItem.getName(),
				selectedItem.getOwner(), selectedItem.getPath(),
				selectedItem.getType().name());
		DeleteItemEvent deleteItemEvent = new DeleteItemEvent(
				itemDescription);
		eventBus.fireEvent(deleteItemEvent);
		Log.debug("Fired: " + deleteItemEvent);
	}

	private void loadData() {
		ItemDescription itemDescription = new ItemDescription(
				selectedItem.getId(), selectedItem.getName(),
				selectedItem.getOwner(), selectedItem.getPath(), selectedItem
						.getType().name());
		MainCodeSetEvent mainCodeSetEvent = new MainCodeSetEvent(
				itemDescription);
		eventBus.fireEvent(mainCodeSetEvent);
		Log.debug("Fired: " + mainCodeSetEvent);

	}

	private void openFile() {
		if (selectedItem != null
				&& !selectedItem.isFolder()) {

			final ItemDescription itemDescription = new ItemDescription(
					selectedItem.getId(), selectedItem.getName(),
					selectedItem.getOwner(), selectedItem.getPath(),
					selectedItem.getType().name());
			
			StatAlgoImporterServiceAsync.INSTANCE.getPublicLink(
					itemDescription, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							if (caught instanceof StatAlgoImporterSessionExpiredException) {
								eventBus.fireEvent(new SessionExpiredEvent(
										SessionExpiredType.EXPIREDONSERVER));
							} else {
								Log.error("Error open file: "
										+ caught.getLocalizedMessage());
								UtilsGXT3.alert("Error",
										caught.getLocalizedMessage());
							}
							caught.printStackTrace();

						}

						@Override
						public void onSuccess(String link) {
							Log.debug("Retrieved link: " + link);
							Window.open(link, itemDescription.getName(), "");
						}

					});

		} else {
			UtilsGXT3.info("Attention", "Select a file!");
		}

	}

}
