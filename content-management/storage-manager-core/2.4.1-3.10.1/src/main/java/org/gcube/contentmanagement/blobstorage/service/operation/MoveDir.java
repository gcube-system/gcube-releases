package org.gcube.contentmanagement.blobstorage.service.operation;

import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.List;

import org.gcube.contentmanagement.blobstorage.resource.MyFile;
import org.gcube.contentmanagement.blobstorage.service.directoryOperation.BucketCoding;
import org.gcube.contentmanagement.blobstorage.service.directoryOperation.DirectoryBucket;
import org.gcube.contentmanagement.blobstorage.transport.TransportManager;
import org.gcube.contentmanagement.blobstorage.transport.TransportManagerFactory;
import org.gcube.contentmanagement.blobstorage.transport.backend.RemoteBackendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveDir extends Operation{
	/**
	 * Logger for this class
	 */
//	private static final GCUBELog logger = new GCUBELog(Download.class);
	final Logger logger=LoggerFactory.getLogger(Download.class);
	private String sourcePath;
	private String destinationPath;
	private OutputStream os;
	public MoveDir(String[] server, String user, String pwd,  String bucket, Monitor monitor, boolean isChunk, String backendType) {
		super(server, user, pwd, bucket, monitor, isChunk, backendType);
	}
	
	public String initOperation(MyFile file, String remotePath,
			String author, String[] server, String rootArea, boolean replaceOption) {
		this.sourcePath=file.getLocalPath();
		this.destinationPath=remotePath;
		sourcePath = new BucketCoding().bucketFileCoding(file.getLocalPath(), rootArea);
		destinationPath = new BucketCoding().bucketFileCoding(remotePath, rootArea);
		return bucket=destinationPath;
		
	}
	
	public String doIt(MyFile myFile) throws RemoteBackendException{
		TransportManagerFactory tmf= new TransportManagerFactory(server, user, password);
		TransportManager tm=tmf.getTransport(backendType);
		List<String>ids=null;
		try {
			ids=tm.moveDir(myFile, sourcePath, destinationPath);
		} catch (UnknownHostException e) {
			tm.close();
			logger.error("Problem in moveDir from: "+sourcePath+" to: "+destinationPath+": "+e.getMessage());
			throw new RemoteBackendException(" Error in moveDir operation ", e.getCause());
		}
		return ids.toString();
	}
	

	@Override
	public String initOperation(MyFile resource, String remotePath,
			String author, String[] server, String rootArea) {
		DirectoryBucket dirBuc=new DirectoryBucket(server, user, password,  remotePath, author);
// For terrastore, the name of bucket is formed: path_____fileName_____author				
		String bucketName=new BucketCoding().bucketFileCoding(remotePath, rootArea);
		this.sourcePath=resource.getLocalPath();
		this.destinationPath=resource.getRemotePath();
		sourcePath = new BucketCoding().bucketFileCoding(resource.getLocalPath(),  rootArea);
		destinationPath = new BucketCoding().bucketFileCoding(resource.getRemotePath(), rootArea);
		return bucket=destinationPath;
	}

}
