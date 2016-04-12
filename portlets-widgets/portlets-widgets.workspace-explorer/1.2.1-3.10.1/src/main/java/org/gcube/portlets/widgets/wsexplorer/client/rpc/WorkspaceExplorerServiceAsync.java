/**
 *
 */
package org.gcube.portlets.widgets.wsexplorer.client.rpc;

import java.util.List;

import org.gcube.portlets.widgets.wsexplorer.shared.FilterCriteria;
import org.gcube.portlets.widgets.wsexplorer.shared.Item;
import org.gcube.portlets.widgets.wsexplorer.shared.ItemCategory;
import org.gcube.portlets.widgets.wsexplorer.shared.ItemInterface;
import org.gcube.portlets.widgets.wsexplorer.shared.ItemType;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The Interface WorkspaceExplorerServiceAsync.
 *
 * @author Francesco Mangiacrapa francesco.mangiacrapa@isti.cnr.it
 * Jun 24, 2015
 */
public interface WorkspaceExplorerServiceAsync {

	/**
	 * Gets the root.
	 *
	 * @param showableTypes
	 *            the showable types
	 * @param purgeEmpyFolders
	 *            the purge empy folders
	 * @param filterCriteria
	 *            the filter criteria
	 * @param callback
	 *            the callback
	 * @return the root
	 */
	public void getRoot(List<ItemType> showableTypes, boolean purgeEmpyFolders,
			FilterCriteria filterCriteria, AsyncCallback<Item> callback);

	/**
	 * Check name.
	 *
	 * @param name
	 *            the name
	 * @param callback
	 *            the callback
	 */
	public void checkName(String name, AsyncCallback<Boolean> callback);

	/**
	 * Gets the folder.
	 *
	 * @param item the item
	 * @param showableTypes the showable types
	 * @param purgeEmpyFolders the purge empy folders
	 * @param filterCriteria the filter criteria
	 * @param callback the callback
	 * @return the folder
	 */
	void getFolder(
		ItemInterface item, List<ItemType> showableTypes,
		boolean purgeEmpyFolders, FilterCriteria filterCriteria,
		AsyncCallback<Item> callback);


	/**
	 * Gets the breadcrumbs by item identifier.
	 *
	 * @param itemIdentifier the item identifier
	 * @param includeItemAsParent the include item as parent
	 * @param asyncCallback the async callback
	 * @return the breadcrumbs by item identifier
	 */
	public void getBreadcrumbsByItemIdentifier(String itemIdentifier,
			boolean includeItemAsParent, AsyncCallback<List<Item>> asyncCallback);

	/**
	 * Gets the my special folder.
	 *
	 * @param showableTypes the showable types
	 * @param purgeEmpyFolders the purge empy folders
	 * @param filterCriteria the filter criteria
	 * @param asyncCallback the async callback
	 * @return the my special folder
	 */
	public void getMySpecialFolder(List<ItemType> showableTypes, boolean purgeEmpyFolders, FilterCriteria filterCriteria, AsyncCallback<Item> asyncCallback);

	/**
	 * Gets the item by category.
	 *
	 * @param category the category
	 * @param asyncCallback the async callback
	 * @return the item by category
	 */
	public void getItemByCategory(ItemCategory category,  AsyncCallback<Item> asyncCallback);

	/**
	 * Gets the size by item id.
	 *
	 * @param id the id
	 * @param asyncCallback the async callback
	 * @return the size by item id
	 */
	public void getSizeByItemId(String id, AsyncCallback<Long> asyncCallback);


	/**
	 * Gets the readable size by item id.
	 *
	 * @param id the id
	 * @param asyncCallback the async callback
	 * @return the readable size by item id
	 */
	public void getReadableSizeByItemId(String id, AsyncCallback<String> asyncCallback);

	/**
	 * Gets the mime type.
	 *
	 * @param id the id
	 * @param asyncCallback the async callback
	 * @return the mime type
	 */
	public void getMimeType(String id, AsyncCallback<String> asyncCallback);

	/**
	 * Gets the user acl for folder id.
	 *
	 * @param id the id
	 * @param asyncCallback the async callback
	 * @return the user acl for folder id
	 */
	public void getUserACLForFolderId(String id,
			AsyncCallback<String> asyncCallback);

	/**
	 * Gets the breadcrumbs by item identifier to parent limit.
	 *
	 * @param itemIdentifier the item identifier
	 * @param parentLimit the parent limit
	 * @param includeItemAsParent the include item as parent
	 * @param callback the callback
	 * @return the breadcrumbs by item identifier to parent limit
	 */
	void getBreadcrumbsByItemIdentifierToParentLimit(String itemIdentifier,
			String parentLimit, boolean includeItemAsParent,
			AsyncCallback<List<Item>> callback);

	/**
	 * Creates the folder.
	 *
	 * @param nameFolder the name folder
	 * @param description the description
	 * @param parentId the parent id
	 * @param callback the callback
	 */
	void createFolder(
		String nameFolder, String description, String parentId,
		AsyncCallback<Item> callback);

}
