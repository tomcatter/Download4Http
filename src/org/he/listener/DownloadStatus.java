package org.he.listener;

import java.io.FileNotFoundException;

import org.he.dto.DonwloadInfor;
import org.he.dto.DownLoadInforData;
import org.he.util.DLog;
import org.he.util.ObjectFileDataUtil;
/**
 * @author BenSon He
 * @email qing878@gmail.com ,qq 277803242
 * @since 19/11/2012
 */
public class DownloadStatus extends Thread {
	private long curSize = 0;
	private long totalSize = 0;
	private boolean isChanged = false;
	private boolean isComplete = false;
	private DownLoadInforData downLoadInforData;
	private String downLoadFileName;
	private String tempFilePath;

	public DownloadStatus(String tempFilePath, String downLoadFileName) {
		downLoadInforData = new DownLoadInforData();
		this.tempFilePath = tempFilePath;
		this.downLoadFileName = downLoadFileName;
	}

	public void addElements(DonwloadInfor donwloadInfor) {
		downLoadInforData.addDwonLoadInforElement(donwloadInfor);
	}

	public void removeCurElement() {
		downLoadInforData.removeCurDwonLoadInforElement();
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	public long getCurSize() {
		return curSize;
	}

	public void setCurSize(long curSize) {
		this.curSize = curSize;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getTempFileName() {
		StringBuffer tempFile = new StringBuffer(tempFilePath);
		tempFile.append(downLoadFileName);
		tempFile.append(".temp");// temp file name type
		return tempFile.toString();
	}

	public synchronized void notifyDownloadStatus(DonwloadInfor donwloadInfor) {
		curSize += donwloadInfor.getBufferSize();
		System.out.println("now have download " + (curSize * 100) / totalSize + "%");
		if (curSize == totalSize)
			this.isComplete = true;
		this.isChanged = true;
	}

	@Override
	public void run() {
		ObjectFileDataUtil objectFileDataUtil = null;
		try {
			objectFileDataUtil = new ObjectFileDataUtil(this.getTempFileName());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			DLog.log(DLog.ERROR, e);
		}
		while (!this.isComplete) {
			if (this.isChanged) {
				objectFileDataUtil.writeObjectToFile(downLoadInforData);
			}
		}
		objectFileDataUtil.removeDataFile();

	}
}
