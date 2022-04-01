package com.revature.utility;

import com.google.cloud.storage.*;
import com.revature.exception.InvalidImageException;
import com.revature.exception.UploadFailedException;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ImageUploadUtility {

    public static String uploadToCloudStorage(InputStream fileInputStream) throws InvalidImageException, IOException, UploadFailedException {
        Tika tika = new Tika();
        String mimeType = tika.detect(fileInputStream);
        if (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png") && !mimeType.equals("image/gif")) {
            throw new InvalidImageException("File format: JPEG, PNG, or GIF");
        }
        String fileName = UUID.randomUUID().toString();
        String projectId = "global-song-344220";
        String bucketName = "employee_reimbursement";
        Storage storage =
                StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(mimeType).build();
        Blob blob = storage.create(blobInfo, fileInputStream);
        if(blob.getMediaLink()==null){
            throw new UploadFailedException("Upload failed");
        }
        String publicUrl = "https://storage.googleapis.com/employee_reimbursement/"+fileName;
        return publicUrl;
    }
}
