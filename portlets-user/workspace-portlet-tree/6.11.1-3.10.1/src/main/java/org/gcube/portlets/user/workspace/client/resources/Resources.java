package org.gcube.portlets.user.workspace.client.resources;

import org.gcube.portlets.user.workspace.client.interfaces.GXTFolderItemTypeEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The Class Resources.
 *
 * @author Francesco Mangiacrapa francesco.mangiacrapa@isti.cnr.it
 */
public class Resources {
	
	public static final Icons ICONS = GWT.create(Icons.class);
	
	private static final String XML = "xml";
	private static final String JAVA = "java";
	private static final String HTML = "html";
	private static final String XHTLXML = "xhtml+xml";
	private static final String GIF = "gif";
	private static final String PNG = "png";
	private static final String JPEG = "jpeg";
	private static final String JPG = "jpg";
	private static final String PDF = "pdf";
	private static final String TIFF = "tiff";
	private static final String SVG = "svg";
	private static final String MSWORD = "msword";
	private static final String DOC = "doc";
	private static final String DOCX = "vnd.openxmlformats-officedocument.wordprocessingml.document";
	private static final String EXCEL = "vnd.ms-excel";
	private static final String TXT = "plain";
	private static final String MPEG = "mpeg";
	private static final String SWF = "swf";
	private static final String FLV = "flv";
	private static final String AVI = "avi";
	private static final String CSV = "csv"; 
	private static final String PPT = "vnd.ms-powerpoint";
	private static final String PPTX = "vnd.openxmlformats-officedocument.presentationml.presentation";
	private static final String XSLX = "vnd.openxmlformats-officedocument.spreadsheetml.sheet"; 
	private static final String ODP = "vnd.oasis.opendocument.presentation"; 
	private static final String XTEX = "x-tex";
	private static final String ZIP = "zip";
	private static final String SEVEN_ZIP = "x-7z-compressed";
	private static final String POSTSCRIPT = "postscript";
	private static final String DVI = "x-dvi";
	private static final String X_SH = "x-sh";
	private static final String X_SHELLSCRIPT = "x-shellscript";
	
	private static final String X_BZIP = "x-bzip";
	private static final String RAR = "x-rar-compressed";
	private static final String GZIP = "gzip";
	
	private static final String WAR ="x-tika-java-web-archive";
	
	/**
	 * Gets the cloud drive icon.
	 *
	 * @return the cloud drive icon
	 */
	public static AbstractImagePrototype getCloudDriveIcon(){
		
		return AbstractImagePrototype.create(ICONS.cloudDrive());  
	}
	
	/**
	 * Gets the icon x tex.
	 *
	 * @return the icon x tex
	 */
	public static AbstractImagePrototype getIconXTex(){
		
		return AbstractImagePrototype.create(ICONS.tex());  
	}
	
	/**
	 * Gets the icon postscript.
	 *
	 * @return the icon postscript
	 */
	public static AbstractImagePrototype getIconPostscript(){
		
		return AbstractImagePrototype.create(ICONS.postscript());  
	}
	
	/**
	 * Gets the icon dvi.
	 *
	 * @return the icon dvi
	 */
	public static AbstractImagePrototype getIconDvi(){
		
		return AbstractImagePrototype.create(ICONS.dvi());  
	}
	

	/**
	 * Gets the icon information.
	 *
	 * @return the icon information
	 */
	public static AbstractImagePrototype getIconInformation(){
		
		return AbstractImagePrototype.create(ICONS.information());  
	}
	
	/**
	 * Gets the icon information.
	 *
	 * @return the icon information
	 */
	public static AbstractImagePrototype getIconAbout(){
		
		return AbstractImagePrototype.create(ICONS.about());  
	}
	
	/**
	 * Gets the icon shell.
	 *
	 * @return the icon shell
	 */
	public static AbstractImagePrototype getIconShell(){
		
		return AbstractImagePrototype.create(ICONS.shell());  
	}
	
	/**
	 * Gets the icon zip.
	 *
	 * @return the icon zip
	 */
	public static AbstractImagePrototype getIconZip(){
		
		return AbstractImagePrototype.create(ICONS.zip());  
	}
	
	
	/**
	 * Gets the icon odp.
	 *
	 * @return the icon odp
	 */
	public static AbstractImagePrototype getIconOdp(){
		
		return AbstractImagePrototype.create(ICONS.odp());  
	}
	
	/**
	 * Gets the icon table.
	 *
	 * @return the icon table
	 */
	public static AbstractImagePrototype getIconTable(){
		
		return AbstractImagePrototype.create(ICONS.table());  
	}
	
	/**
	 * Gets the icon recycle.
	 *
	 * @return the icon recycle
	 */
	public static AbstractImagePrototype getIconRecycle(){
		
		return AbstractImagePrototype.create(ICONS.recycle());  
	}

	/**
	 * Gets the icon undo.
	 *
	 * @return the icon undo
	 */
	public static AbstractImagePrototype getIconUndo(){
	
		return AbstractImagePrototype.create(ICONS.undo());  
	}
	

	/**
	 * Gets the icon archive.
	 *
	 * @return the icon archive
	 */
	public static AbstractImagePrototype getIconArchive(){
	
		return AbstractImagePrototype.create(ICONS.archive());  
	}
	
	/**
	 * Gets the icon link.
	 *
	 * @return the icon link
	 */
	public static AbstractImagePrototype getIconLink(){
		return AbstractImagePrototype.create(ICONS.extLink());  
	}
		
	/**
	 * Gets the icon share link.
	 *
	 * @return the icon share link
	 */
	public static AbstractImagePrototype getIconShareLink(){
		
		return AbstractImagePrototype.create(ICONS.shareLink());  
	}
	
	/**
	 * Gets the trash full.
	 *
	 * @return the trash full
	 */
	public static AbstractImagePrototype getTrashFull(){
		
		return AbstractImagePrototype.create(ICONS.trash_full());  
	}
	
	/**
	 * Gets the trash empty.
	 *
	 * @return the trash empty
	 */
	public static AbstractImagePrototype getTrashEmpty(){
		
		return AbstractImagePrototype.create(ICONS.trash_empty());  
	}
	
	/**
	 * Gets the icon create new.
	 *
	 * @return the icon create new
	 */
	public static AbstractImagePrototype getIconCreateNew(){
		
		return AbstractImagePrototype.create(ICONS.createNew());  
	}
	
	/**
	 * Gets the icon vre folder.
	 *
	 * @return the icon vre folder
	 */
	public static AbstractImagePrototype getIconVREFolder(){
		
		return AbstractImagePrototype.create(ICONS.vreFolder());  
	}
	
	/**
	 * Gets the icon ppt.
	 *
	 * @return the icon ppt
	 */
	public static AbstractImagePrototype getIconPpt(){
		
		return AbstractImagePrototype.create(ICONS.ppt());  
	}
	
	/**
	 * Gets the icon excel.
	 *
	 * @return the icon excel
	 */
	public static AbstractImagePrototype getIconExcel(){
		
		return AbstractImagePrototype.create(ICONS.excel());  
	}
	
	/**
	 * Gets the icon users.
	 *
	 * @return the icon users
	 */
	public static AbstractImagePrototype getIconUsers(){
		
		return AbstractImagePrototype.create(ICONS.users());  
	}
	
	/**
	 * Gets the icon gcube item.
	 *
	 * @return the icon gcube item
	 */
	public static AbstractImagePrototype getIconGcubeItem(){
		
		return AbstractImagePrototype.create(ICONS.gcubeItem());  
	}
	
	/**
	 * Gets the icon share user.
	 *
	 * @return the icon share user
	 */
	public static AbstractImagePrototype getIconShareUser(){
		
		return AbstractImagePrototype.create(ICONS.user());  
	}
	
	/**
	 * Gets the icon share group.
	 *
	 * @return the icon share group
	 */
	public static AbstractImagePrototype getIconShareGroup(){
		
		return AbstractImagePrototype.create(ICONS.groupusers());  
	}
	
	/**
	 * Gets the icon special folder.
	 *
	 * @return the icon special folder
	 */
	public static AbstractImagePrototype getIconSpecialFolder(){
		
		return AbstractImagePrototype.create(ICONS.specialFolder());  
	}
	
	/**
	 * Gets the icon administrator.
	 *
	 * @return the icon administrator
	 */
	public static AbstractImagePrototype getIconAdministrator(){
		
		return AbstractImagePrototype.create(ICONS.administrator());  
	}
	
	/**
	 * Gets the icon cut.
	 *
	 * @return the icon cut
	 */
	public static AbstractImagePrototype getIconCut(){
		
		return AbstractImagePrototype.create(ICONS.cut());  
	}
	
	/**
	 * Gets the icon info.
	 *
	 * @return the icon info
	 */
	public static AbstractImagePrototype getIconInfo(){
		
		return AbstractImagePrototype.create(ICONS.info());  
	}
	
	/**
	 * Gets the icon shared folder.
	 *
	 * @return the icon shared folder
	 */
	public static AbstractImagePrototype getIconSharedFolder(){
		
		return AbstractImagePrototype.create(ICONS.sharedFolder());  
	}
	
	/**
	 * Gets the icon csv.
	 *
	 * @return the icon csv
	 */
	public static AbstractImagePrototype getIconCsv(){
		
		return AbstractImagePrototype.create(ICONS.csv());  
	}
	
	/**
	 * Gets the icon read.
	 *
	 * @return the icon read
	 */
	public static AbstractImagePrototype getIconRead(){
		
		return AbstractImagePrototype.create(ICONS.read());  
	}
	
	/**
	 * Gets the icon not read.
	 *
	 * @return the icon not read
	 */
	public static AbstractImagePrototype getIconNotRead(){
		
		return AbstractImagePrototype.create(ICONS.notread());  
	}


	/**
	 * Gets the close icon.
	 *
	 * @return the close icon
	 */
	public static AbstractImagePrototype getCloseIcon(){
		
		return AbstractImagePrototype.create(ICONS.close());  
	}
	
	/**
	 * Gets the icon gif.
	 *
	 * @return the icon gif
	 */
	public static AbstractImagePrototype getIconGif(){
		
		return AbstractImagePrototype.create(ICONS.gif());  
	} 
	
	/**
	 * Gets the icon jpeg.
	 *
	 * @return the icon jpeg
	 */
	public static AbstractImagePrototype getIconJpeg(){
		
		return AbstractImagePrototype.create(ICONS.jpeg());  
	} 
	
	/**
	 * Gets the icon svg.
	 *
	 * @return the icon svg
	 */
	public static AbstractImagePrototype getIconSvg(){
		
		return AbstractImagePrototype.create(ICONS.svg());  
	} 
	
	/**
	 * Gets the icon png.
	 *
	 * @return the icon png
	 */
	public static AbstractImagePrototype getIconPng(){
		
		return AbstractImagePrototype.create(ICONS.png());  
	} 
	
	/**
	 * Gets the icon tiff.
	 *
	 * @return the icon tiff
	 */
	public static AbstractImagePrototype getIconTiff(){
		
		return AbstractImagePrototype.create(ICONS.tiff());  
	} 
	
	/**
	 * Gets the icon pdf.
	 *
	 * @return the icon pdf
	 */
	public static AbstractImagePrototype getIconPdf(){
		
		return AbstractImagePrototype.create(ICONS.pdf());  
	}
	
	/**
	 * Gets the icon xml.
	 *
	 * @return the icon xml
	 */
	public static AbstractImagePrototype getIconXml(){
		
		return AbstractImagePrototype.create(ICONS.xml());  
	} 
	
	/**
	 * Gets the icon html.
	 *
	 * @return the icon html
	 */
	public static AbstractImagePrototype getIconHtml(){
		
		return AbstractImagePrototype.create(ICONS.html());  
	} 
	
	/**
	 * Gets the icon java.
	 *
	 * @return the icon java
	 */
	public static AbstractImagePrototype getIconJava(){
		
		return AbstractImagePrototype.create(ICONS.java());  
	} 
	
	/**
	 * Gets the icon doc.
	 *
	 * @return the icon doc
	 */
	public static AbstractImagePrototype getIconDoc(){
		
		return AbstractImagePrototype.create(ICONS.doc());  
	} 
	
	/**
	 * Gets the icon txt.
	 *
	 * @return the icon txt
	 */
	public static AbstractImagePrototype getIconTxt(){
		
		return AbstractImagePrototype.create(ICONS.txt());  
	} 
	
	/**
	 * Gets the icon movie.
	 *
	 * @return the icon movie
	 */
	public static AbstractImagePrototype getIconMovie(){
		
		return AbstractImagePrototype.create(ICONS.movie());  
	} 
	
	/**
	 * Gets the icon add folder.
	 *
	 * @return the icon add folder
	 */
	public static AbstractImagePrototype getIconAddFolder(){
		
		return AbstractImagePrototype.create(ICONS.addFolder());  
	} 
	
	/**
	 * Gets the icon add folder32.
	 *
	 * @return the icon add folder32
	 */
	public static AbstractImagePrototype getIconAddFolder32(){
		
		return AbstractImagePrototype.create(ICONS.addFolder32());  
	} 
	
	/**
	 * Gets the icon rename item.
	 *
	 * @return the icon rename item
	 */
	public static AbstractImagePrototype getIconRenameItem(){
		
		return AbstractImagePrototype.create(ICONS.renameItem());  
	} 
	
	/**
	 * Gets the icon rename item32.
	 *
	 * @return the icon rename item32
	 */
	public static AbstractImagePrototype getIconRenameItem32(){
		
		return AbstractImagePrototype.create(ICONS.renameItem32());  
	} 
	
	/**
	 * Gets the icon file upload.
	 *
	 * @return the icon file upload
	 */
	public static AbstractImagePrototype getIconFileUpload(){
		
		return AbstractImagePrototype.create(ICONS.uploadFile());  
	} 
	
	/**
	 * Gets the icon file upload32.
	 *
	 * @return the icon file upload32
	 */
	public static AbstractImagePrototype getIconFileUpload32(){
		
		return AbstractImagePrototype.create(ICONS.uploadFile32());  
	} 
	
	/**
	 * Gets the icon delete folder.
	 *
	 * @return the icon delete folder
	 */
	public static AbstractImagePrototype getIconDeleteFolder(){
		
		return AbstractImagePrototype.create(ICONS.deleteFolder());  
	} 
	
	/**
	 * Gets the icon delete item.
	 *
	 * @return the icon delete item
	 */
	public static AbstractImagePrototype getIconDeleteItem(){
		
		return AbstractImagePrototype.create(ICONS.deleteItem());  
	} 
	
	/**
	 * Gets the icon delete item32.
	 *
	 * @return the icon delete item32
	 */
	public static AbstractImagePrototype getIconDeleteItem32(){
		
		return AbstractImagePrototype.create(ICONS.deleteItem32());  
	} 
	
	/**
	 * Gets the icon folder.
	 *
	 * @return the icon folder
	 */
	public static AbstractImagePrototype getIconFolder(){
		
		return AbstractImagePrototype.create(ICONS.folder());  
	} 
	
	/**
	 * Gets the icon audio.
	 *
	 * @return the icon audio
	 */
	public static AbstractImagePrototype getIconAudio(){
		
		return AbstractImagePrototype.create(ICONS.audio());  
	} 
	
	/**
	 * Gets the icon archive upload.
	 *
	 * @return the icon archive upload
	 */
	public static AbstractImagePrototype getIconArchiveUpload(){
		
		return AbstractImagePrototype.create(ICONS.archiveUpload());  
	} 
	
	/**
	 * Gets the icon biodiversity.
	 *
	 * @return the icon biodiversity
	 */
	public static AbstractImagePrototype getIconBiodiversity(){
		
		return AbstractImagePrototype.create(ICONS.biodiversity());  
	} 
	
	/**
	 * Gets the icon images.
	 *
	 * @return the icon images
	 */
	public static AbstractImagePrototype getIconImages(){
		
		return AbstractImagePrototype.create(ICONS.images());  
	} 
	
	/**
	 * Gets the icon documents.
	 *
	 * @return the icon documents
	 */
	public static AbstractImagePrototype getIconDocuments(){
		
		return AbstractImagePrototype.create(ICONS.documents());  
	} 
	
	/**
	 * Gets the icon pencil.
	 *
	 * @return the icon pencil
	 */
	public static AbstractImagePrototype getIconPencil(){
		
		return AbstractImagePrototype.create(ICONS.pencil());  
	} 
	
	/**
	 * Gets the icon edit.
	 *
	 * @return the icon edit
	 */
	public static AbstractImagePrototype getIconEdit(){
		
		return AbstractImagePrototype.create(ICONS.edit());  
	} 
	
	/**
	 * Gets the icon history.
	 *
	 * @return the icon history
	 */
	public static AbstractImagePrototype getIconHistory(){
		
		return AbstractImagePrototype.create(ICONS.history());  
	} 
	
	/**
	 * Gets the icon search.
	 *
	 * @return the icon search
	 */
	public static AbstractImagePrototype getIconSearch() {
		
		return AbstractImagePrototype.create(ICONS.search());  
	}
	

	/**
	 * Gets the icon search ws.
	 *
	 * @return the icon search ws
	 */
	public static AbstractImagePrototype getIconSearchWs() {
		
		return AbstractImagePrototype.create(ICONS.search2());  
	}
	
	
	/**
	 * Gets the icon links.
	 *
	 * @return the icon links
	 */
	public static AbstractImagePrototype getIconLinks(){
		
		return AbstractImagePrototype.create(ICONS.links());  
	} 
	
	/**
	 * Gets the icon report.
	 *
	 * @return the icon report
	 */
	public static AbstractImagePrototype getIconReport(){
		return AbstractImagePrototype.create(ICONS.report());  
	} 
	
	/**
	 * Gets the icon grid view.
	 *
	 * @return the icon grid view
	 */
	public static AbstractImagePrototype getIconGridView(){
		return AbstractImagePrototype.create(ICONS.gridView());  
	} 
	
	/**
	 * Gets the icon report template.
	 *
	 * @return the icon report template
	 */
	private static AbstractImagePrototype getIconReportTemplate() {
		return AbstractImagePrototype.create(ICONS.reportTemplate());  
	}

	/**
	 * Gets the icon time series.
	 *
	 * @return the icon time series
	 */
	public static AbstractImagePrototype getIconTimeSeries(){
		return AbstractImagePrototype.create(ICONS.timeSeries());  
	} 
	
	/**
	 * Gets the icon download.
	 *
	 * @return the icon download
	 */
	public static AbstractImagePrototype getIconDownload(){
		return AbstractImagePrototype.create(ICONS.download());  
	} 
	
	/**
	 * Gets the icon cancel.
	 *
	 * @return the icon cancel
	 */
	public static AbstractImagePrototype getIconCancel(){
		return AbstractImagePrototype.create(ICONS.cancel());  
	} 
	
	/**
	 * Gets the icon toggle list.
	 *
	 * @return the icon toggle list
	 */
	public static AbstractImagePrototype getIconToggleList() {
		return AbstractImagePrototype.create(ICONS.toggleList());  
	}
	
	/**
	 * Gets the icon toggle group.
	 *
	 * @return the icon toggle group
	 */
	public static AbstractImagePrototype getIconToggleGroup() {
		return AbstractImagePrototype.create(ICONS.toggleGroup());  
	}
	
	/**
	 * Gets the icon toggle icon.
	 *
	 * @return the icon toggle icon
	 */
	public static AbstractImagePrototype getIconToggleIcon() {
		return AbstractImagePrototype.create(ICONS.toggleIcon());  
	}
	
	/**
	 * Gets the icon save.
	 *
	 * @return the icon save
	 */
	public static AbstractImagePrototype getIconSave() {
		return AbstractImagePrototype.create(ICONS.save());  
	}
	
	/**
	 * Gets the icon star.
	 *
	 * @return the icon star
	 */
	public static AbstractImagePrototype getIconStar() {
		return AbstractImagePrototype.create(ICONS.star());  
	}
	
	/**
	 * Gets the icon preview.
	 *
	 * @return the icon preview
	 */
	public static AbstractImagePrototype getIconPreview() {
		return AbstractImagePrototype.create(ICONS.preview());  
	}
	
	/**
	 * Gets the icon show.
	 *
	 * @return the icon show
	 */
	public static AbstractImagePrototype getIconShow() {
		return AbstractImagePrototype.create(ICONS.show());  
	}
	
	/**
	 * Gets the icon open url.
	 *
	 * @return the icon open url
	 */
	public static AbstractImagePrototype getIconOpenUrl() {
		return AbstractImagePrototype.create(ICONS.openUrl());  
	}
	
	/**
	 * Gets the icon add url.
	 *
	 * @return the icon add url
	 */
	public static AbstractImagePrototype getIconAddUrl() {
		return AbstractImagePrototype.create(ICONS.addUrl());  
	}
	
	/**
	 * Gets the icon send to.
	 *
	 * @return the icon send to
	 */
	public static AbstractImagePrototype getIconSendTo() {
		return AbstractImagePrototype.create(ICONS.sendTo());  
	}
	
	/**
	 * Gets the icon check user.
	 *
	 * @return the icon check user
	 */
	public static AbstractImagePrototype getIconCheckUser() {
		return AbstractImagePrototype.create(ICONS.checkUser());  
	}
	
	/**
	 * Gets the icon messages received.
	 *
	 * @return the icon messages received
	 */
	public static AbstractImagePrototype getIconMessagesReceived() {
		return AbstractImagePrototype.create(ICONS.inboxReceived());  
	}

	/**
	 * Gets the icon messages sent.
	 *
	 * @return the icon messages sent
	 */
	public static AbstractImagePrototype getIconMessagesSent() {
		return AbstractImagePrototype.create(ICONS.inboxSent());  
	}
	
	/**
	 * Gets the icon email.
	 *
	 * @return the icon email
	 */
	public static AbstractImagePrototype getIconEmail() {
		return AbstractImagePrototype.create(ICONS.email());  
	}
	
	/**
	 * Gets the icon open email.
	 *
	 * @return the icon open email
	 */
	public static AbstractImagePrototype getIconOpenEmail() {
		return AbstractImagePrototype.create(ICONS.openEmail());  
	}

	/**
	 * Gets the icon hand.
	 *
	 * @return the icon hand
	 */
	public static AbstractImagePrototype getIconHand() {
		return AbstractImagePrototype.create(ICONS.hand());  
	}
	
	/**
	 * Gets the icon save attachments.
	 *
	 * @return the icon save attachments
	 */
	public static AbstractImagePrototype getIconSaveAttachments() {
		return AbstractImagePrototype.create(ICONS.saveAttachs());  
	}
	
	/**
	 * Gets the icon download emails.
	 *
	 * @return the icon download emails
	 */
	public static AbstractImagePrototype getIconDownloadEmails() {
		return AbstractImagePrototype.create(ICONS.downloadEmail());  
	}

	/**
	 * Gets the icon email read.
	 *
	 * @return the icon email read
	 */
	public static AbstractImagePrototype getIconEmailRead() {
		return AbstractImagePrototype.create(ICONS.emailRead()); 
	}

	/**
	 * Gets the icon email not read.
	 *
	 * @return the icon email not read
	 */
	public static AbstractImagePrototype getIconEmailNotRead() {
		return AbstractImagePrototype.create(ICONS.emailNotRead()); 
	}
	
	/**
	 * Gets the icon delete message.
	 *
	 * @return the icon delete message
	 */
	public static AbstractImagePrototype getIconDeleteMessage() {
		return AbstractImagePrototype.create(ICONS.emailDelete()); 
	}
	
	/**
	 * Gets the icon email forward.
	 *
	 * @return the icon email forward
	 */
	public static AbstractImagePrototype getIconEmailForward() {
		return AbstractImagePrototype.create(ICONS.emailForward());
	}
	
	/**
	 * Gets the icon copy.
	 *
	 * @return the icon copy
	 */
	public static AbstractImagePrototype getIconCopy() {
		return AbstractImagePrototype.create(ICONS.copy());
	}

	/**
	 * Gets the icon paste.
	 *
	 * @return the icon paste
	 */
	public static AbstractImagePrototype getIconPaste() {
		return AbstractImagePrototype.create(ICONS.paste());
	}
	
	/**
	 * Gets the icon refresh.
	 *
	 * @return the icon refresh
	 */
	public static AbstractImagePrototype getIconRefresh() {
		return AbstractImagePrototype.create(ICONS.refreshFolder());
	}
	
	/**
	 * Gets the icon bulk update.
	 *
	 * @return the icon bulk update
	 */
	public static AbstractImagePrototype getIconBulkUpdate() {
		return AbstractImagePrototype.create(ICONS.loading2());
	}
	
	/**
	 * Gets the icon loading.
	 *
	 * @return the icon loading
	 */
	public static AbstractImagePrototype getIconLoading() {
		return AbstractImagePrototype.create(ICONS.loading());
	}

	/**
	 * Gets the icon loading off.
	 *
	 * @return the icon loading off
	 */
	public static AbstractImagePrototype getIconLoadingOff() {
		return AbstractImagePrototype.create(ICONS.loadingOff());
	}
	
	/**
	 * Gets the icon loading2.
	 *
	 * @return the icon loading2
	 */
	public static AbstractImagePrototype getIconLoading2() {
		return AbstractImagePrototype.create(ICONS.loading2());
	}
	
	/**
	 * Gets the icon delete2.
	 *
	 * @return the icon delete2
	 */
	public static AbstractImagePrototype getIconDelete2() {
		return AbstractImagePrototype.create(ICONS.delete2());
	}

	/**
	 * Gets the icon url web dav.
	 *
	 * @return the icon url web dav
	 */
	public static AbstractImagePrototype getIconUrlWebDav() {
		return AbstractImagePrototype.create(ICONS.urlWebDav());
	}
	
	/**
	 * Gets the icon remove filter.
	 *
	 * @return the icon remove filter
	 */
	public static AbstractImagePrototype getIconRemoveFilter() {
		return AbstractImagePrototype.create(ICONS.removeFilter());
	}
	
	/**
	 * Gets the icon new mail.
	 *
	 * @return the icon new mail
	 */
	public static AbstractImagePrototype getIconNewMail() {
		return AbstractImagePrototype.create(ICONS.createNewMail());
	}
	
	/**
	 * Gets the icon reply mail.
	 *
	 * @return the icon reply mail
	 */
	public static AbstractImagePrototype getIconReplyMail() {
		return AbstractImagePrototype.create(ICONS.replyMail());
	}
	
	/**
	 * Gets the icon public link.
	 *
	 * @return the icon public link
	 */
	public static AbstractImagePrototype getIconPublicLink() {
		return AbstractImagePrototype.create(ICONS.publicLink());
	}
	
	/**
	 * Gets the icon reply all mail.
	 *
	 * @return the icon reply all mail
	 */
	public static AbstractImagePrototype getIconReplyAllMail() {
		return AbstractImagePrototype.create(ICONS.replyAllMail());
	}
	
	/**
	 * Gets the icon workflow report.
	 *
	 * @return the icon workflow report
	 */
	public static AbstractImagePrototype getIconWorkflowReport() {
		return AbstractImagePrototype.create(ICONS.workflowReport());
	}

	/**
	 * Gets the icon workflow template.
	 *
	 * @return the icon workflow template
	 */
	public static AbstractImagePrototype getIconWorkflowTemplate() {
		return AbstractImagePrototype.create(ICONS.workflowTemplate());
	}

	/**
	 * Gets the icon web dav.
	 *
	 * @return the icon web dav
	 */
	public static AbstractImagePrototype getIconWebDav() {
		return AbstractImagePrototype.create(ICONS.webDav());
	}
	
	/**
	 * Gets the icon resource link.
	 *
	 * @return the icon resource link
	 */
	public static AbstractImagePrototype getIconResourceLink() {
		return AbstractImagePrototype.create(ICONS.resourceLink());
	}

	/**
	 * Gets the icon share folder.
	 *
	 * @return the icon share folder
	 */
	public static AbstractImagePrototype getIconShareFolder() {
		return AbstractImagePrototype.create(ICONS.shareFolder());
	}
	
	/**
	 * Gets the icon un share folder.
	 *
	 * @return the icon un share folder
	 */
	public static AbstractImagePrototype getIconUnShareFolder() {
		return AbstractImagePrototype.create(ICONS.unShareFolder());
	}
	
	/**
	 * Gets the icon un share user.
	 *
	 * @return the icon un share user
	 */
	public static AbstractImagePrototype getIconUnShareUser() {
		return AbstractImagePrototype.create(ICONS.unShareUser());
	}
	
	/**
	 * Gets the icon write own.
	 *
	 * @return the icon write own
	 */
	public static AbstractImagePrototype getIconWriteOwn() {
		return AbstractImagePrototype.create(ICONS.writeown());
	}
	
	/**
	 * Gets the icon write all.
	 *
	 * @return the icon write all
	 */
	public static AbstractImagePrototype getIconWriteAll() {
		return AbstractImagePrototype.create(ICONS.writeall());
	}
	
	/**
	 * Gets the icon read only.
	 *
	 * @return the icon read only
	 */
	public static AbstractImagePrototype getIconReadOnly() {
		return AbstractImagePrototype.create(ICONS.readonly());
	}
	
	/**
	 * Gets the all left.
	 *
	 * @return the all left
	 */
	public static AbstractImagePrototype getAllLeft() {
		return AbstractImagePrototype.create(ICONS.allLeft());
	}
	
	/**
	 * Gets the all right.
	 *
	 * @return the all right
	 */
	public static AbstractImagePrototype getAllRight() {
		return AbstractImagePrototype.create(ICONS.allRight());
	}
	
	/**
	 * Gets the selected left.
	 *
	 * @return the selected left
	 */
	public static AbstractImagePrototype getSelectedLeft() {
		return AbstractImagePrototype.create(ICONS.selectedLeft());
	}
	
	/**
	 * Gets the selected right.
	 *
	 * @return the selected right
	 */
	public static AbstractImagePrototype getSelectedRight() {
		return AbstractImagePrototype.create(ICONS.selectedRight());
	}
	
	/**
	 * Gets the icon permissions.
	 *
	 * @return the icon permissions
	 */
	public static AbstractImagePrototype getIconPermissions() {
		return AbstractImagePrototype.create(ICONS.permissions());
	}
	
	
	/**
	 * Gets the icon add administrator.
	 *
	 * @return the icon add administrator
	 */
	public static AbstractImagePrototype getIconAddAdministrator() {
		return AbstractImagePrototype.create(ICONS.addAdmin());
	}
	
	/**
	 * Gets the icon manage administrator.
	 *
	 * @return the icon manage administrator
	 */
	public static AbstractImagePrototype getIconManageAdministrator() {
		return AbstractImagePrototype.create(ICONS.manageAdmin());
	}

	//ImageResources
	/**
	 * Gets the image path separator.
	 *
	 * @return the image path separator
	 */
	public static ImageResource getImagePathSeparator(){
		return ICONS.separatorPath();
	}
	
	/**
	 * Gets the image loading.
	 *
	 * @return the image loading
	 */
	public static ImageResource getImageLoading() {
		return ICONS.loading();
	}

	/**
	 * Gets the image hard disk.
	 *
	 * @return the image hard disk
	 */
	public static ImageResource getImageHardDisk(){
		return ICONS.hardDisk();
	}
	
	/**
	 * Gets the image search.
	 *
	 * @return the image search
	 */
	public static ImageResource getImageSearch(){
		return ICONS.search();
	}
	
	/**
	 * Gets the image folder.
	 *
	 * @return the image folder
	 */
	public static ImageResource getImageFolder(){
		return ICONS.folder();
	}
	
	/**
	 * Gets the image cancel.
	 *
	 * @return the image cancel
	 */
	public static ImageResource getImageCancel() {
		return ICONS.cancel();
	}
	
	/**
	 * Gets the image delete.
	 *
	 * @return the image delete
	 */
	public static ImageResource getImageDelete() {
		return ICONS.delete2();
	}
	
	/**
	 * Gets the image attachs.
	 *
	 * @return the image attachs
	 */
	public static ImageResource getImageAttachs() {
		return ICONS.attach();  
	}
	


	/**
	 * Gets the icon by media type name.
	 *
	 * @param name the name
	 * @param mediaTypeName the media type name
	 * @return the icon by media type name
	 */
	public static AbstractImagePrototype getIconByMediaTypeName(String name, String mediaTypeName) {

		if (MPEG.equals(mediaTypeName) || SWF.equals(mediaTypeName) || FLV.equals(mediaTypeName) || AVI.equals(mediaTypeName)) {
			return Resources.getIconMovie();
		} else if (JPEG.equals(mediaTypeName) || JPG.equals(mediaTypeName)) {
			return Resources.getIconJpeg();
		} else if (XHTLXML.equals(mediaTypeName)) {
			return Resources.getIconXml();
		} else if (mediaTypeName.contains(MSWORD) || DOC.equals(mediaTypeName) || mediaTypeName.contains(DOCX)) {
			return Resources.getIconDoc();
		} else if (XML.equals(mediaTypeName)) {
			return Resources.getIconXml();
		} else if (CSV.equals(mediaTypeName)) {
			return Resources.getIconCsv();
		} else if (JAVA.equals(mediaTypeName)) {
			return Resources.getIconJava();
		} else if (HTML.equals(mediaTypeName)) {
			return Resources.getIconHtml();
		} else if (PNG.equals(mediaTypeName)) {
			return Resources.getIconPng();
		} else if (PDF.equals(mediaTypeName)) {
			return Resources.getIconPdf();
		} else if (TIFF.equals(mediaTypeName)) {
			return Resources.getIconTiff();
		} else if (SVG.equals(mediaTypeName)) {
			return Resources.getIconSvg();
		} else if (GIF.equals(mediaTypeName)) {
			return Resources.getIconGif();
		} else if (TXT.equals(mediaTypeName)) {
			return Resources.getIconTxt();
		}else if (mediaTypeName.contains(PPT) || mediaTypeName.equals(PPTX)) {
			return Resources.getIconPpt();
		} else if(mediaTypeName.contains(ODP)){
			return Resources.getIconOdp();
		}else if (mediaTypeName.contains(EXCEL) || mediaTypeName.contains(XSLX)) {
			return Resources.getIconExcel();
		}else if (mediaTypeName.contains(ZIP) || mediaTypeName.contains(SEVEN_ZIP)) {
			int fe = name.lastIndexOf(".");
			String fileExtension = name.substring(fe+1, name.length());
			if(fileExtension.compareTo("jar")==0)
				return Resources.getIconJava();
			
			return Resources.getIconZip();
		}else if (mediaTypeName.contains(DVI)) {
				return Resources.getIconDvi();
		}else if (mediaTypeName.contains(X_SH) || mediaTypeName.contains(X_SHELLSCRIPT)) {
			return Resources.getIconShell();
		}else if (mediaTypeName.contains(XTEX)) {
			return Resources.getIconXTex();
		}else if (mediaTypeName.contains(POSTSCRIPT)) {
			return Resources.getIconPostscript();
		}else if (mediaTypeName.contains(RAR) || mediaTypeName.contains(GZIP) || mediaTypeName.contains(X_BZIP)) {
			return Resources.getIconArchive();
		}else if (mediaTypeName.contains(WAR)) {
			return Resources.getIconJava();
		}
		return Resources.getIconTable();
	}

	
	/**
	 * Gets the icon by folder item type.
	 *
	 * @param itemType the item type
	 * @return the icon by folder item type
	 */
	public static AbstractImagePrototype getIconByFolderItemType(GXTFolderItemTypeEnum itemType){
		
		if(itemType!=null){
		
			if(itemType.equals(GXTFolderItemTypeEnum.ANNOTATION)){
				return Resources.getIconTxt();
			}else if(itemType.equals(GXTFolderItemTypeEnum.DOCUMENT)){
				return Resources.getIconTxt();
			}else if(itemType.equals(GXTFolderItemTypeEnum.EXTERNAL_FILE)){
				return Resources.getIconTable();
			}else if(itemType.equals(GXTFolderItemTypeEnum.EXTERNAL_IMAGE)){
				return Resources.getIconJpeg();
			}else if(itemType.equals(GXTFolderItemTypeEnum.EXTERNAL_PDF_FILE)){
				return Resources.getIconPdf();
			}else if(itemType.equals(GXTFolderItemTypeEnum.EXTERNAL_URL)){
				return Resources.getIconLink();
			}else if(itemType.equals(GXTFolderItemTypeEnum.IMAGE_DOCUMENT)){
				return Resources.getIconPng();
			}else if(itemType.equals(GXTFolderItemTypeEnum.METADATA)){
				return Resources.getIconSvg();
			}else if(itemType.equals(GXTFolderItemTypeEnum.PDF_DOCUMENT)){
				return Resources.getIconPdf();
			}else if(itemType.equals(GXTFolderItemTypeEnum.GCUBE_ITEM)){
				return Resources.getIconGcubeItem();
	//		}else if(itemType.equals(GXTFolderItemTypeEnum.QUERY)){
	//			return Resources.getIconTable();
			}else if(itemType.equals(GXTFolderItemTypeEnum.TIME_SERIES)){
				return Resources.getIconTimeSeries();
			}else if(itemType.equals(GXTFolderItemTypeEnum.REPORT)){
				return Resources.getIconReport();
			}else if(itemType.equals(GXTFolderItemTypeEnum.REPORT_TEMPLATE)){
				return Resources.getIconReportTemplate();
			}else if(itemType.equals(GXTFolderItemTypeEnum.URL_DOCUMENT)){
				return Resources.getIconLink();
			}else if(itemType.equals(GXTFolderItemTypeEnum.WORKFLOW_REPORT)){
				return Resources.getIconWorkflowReport();
			}else if(itemType.equals(GXTFolderItemTypeEnum.WORKFLOW_TEMPLATE)){
				return Resources.getIconWorkflowTemplate();
			}else if(itemType.equals(GXTFolderItemTypeEnum.FOLDER)){
				return Resources.getIconFolder();
			}else if(itemType.equals(GXTFolderItemTypeEnum.EXTERNAL_RESOURCE_LINK)){
				return Resources.getIconResourceLink();
			}
		}
		return Resources.getIconTable();
	}


	/**
	 * Gets the icon by type.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the icon by type
	 */
	public static AbstractImagePrototype getIconByType(String name, String type){
		
		if(type.equals(GXTFolderItemTypeEnum.FOLDER.toString()))
			return Resources.getIconFolder();
		
		//RECOVERING "media type name" from type / media type name [+suffix]
		int sl = type.indexOf("/");
		String mediaTypeName = type.substring(sl+1, type.length());
		
		return Resources.getIconByMediaTypeName(name, mediaTypeName);
	}
	
}
