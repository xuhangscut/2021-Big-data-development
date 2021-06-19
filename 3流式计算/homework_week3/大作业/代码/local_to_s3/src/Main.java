import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bingocloud.AmazonClientException;
import com.bingocloud.AmazonServiceException;
import com.bingocloud.auth.BasicAWSCredentials;
import com.bingocloud.services.s3.AmazonS3Client;


public class Main {
    private final static String bucketName = "xuhang";
    private final static String prefix = "upload/";
    private final static String filePath   = "D:/STUDY/大三下/大数据实训/class3/outputData2";
    private final static String accessKey = "64ABEA89A8CFB8297072";
    private final static String secretKey = "W0Y0ODdDN0I3MjkwNDcyQTlBODU4REE4OEMzNDRC";
    private final static String serviceEndpoint =
            "http://scut.depts.bingosoft.net:29997";
    private final static String signingRegion = "";
    private static List<File> getAllFileList(String filePath, List<File> fileList){
        if (fileList == null){
            fileList = new ArrayList<File>();
        }
        File rootFile = new File(filePath);
        File[] files = rootFile.listFiles();
        if (files != null){
            for (File file : files){
                if (file.isDirectory()){
                    fileList.add(file);
                    getAllFileList(file.getAbsolutePath(), fileList);
                }
                else{
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }
    private static List<String> getLocalpaths(List<File> localfiles){
        List<String> lpaths =new ArrayList<String>();
        for(File file:localfiles) {
            if(file.isDirectory()) {
                continue;
            }
            String lpath = file.getPath();
            lpath = lpath.replace('\\', '/');
            lpaths.add(lpath);
        }
        return lpaths;
    }
    private static void upload(AmazonS3Client s3,String path,String bucketName,String filePath) {
        final String keyName = path.substring(filePath.length());
        final File file = new File(path);

        for (int i = 0; i < 2; i++) {
            try {
                s3.putObject(bucketName, prefix+keyName, file);
                break;
            } catch (AmazonServiceException e) {
                if (e.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                    s3.createBucket(bucketName);
                    continue;
                }

                System.err.println(e.toString());
                System.exit(1);
            } catch (AmazonClientException e) {
                try {
                    // detect bucket whether exists
                    s3.getBucketAcl(bucketName);
                } catch (AmazonServiceException ase) {
                    if (ase.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {
                        s3.createBucket(bucketName);
                        continue;
                    }
                } catch (Exception ignore) {
                }

                System.err.println(e.toString());
                System.exit(1);
            }
        }

        System.out.println("Done!");

    }
    public static void local2s3(String bucketName, String filePath){
        //初始化S3授权信息
        final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        final AmazonS3Client client = new AmazonS3Client((credentials));
        client.setEndpoint(serviceEndpoint);
        //取出本地目录下所有文件
        List<File> localfiles = getAllFileList(filePath,null);
        List<String> lpaths =getLocalpaths(localfiles);
        for(String path:lpaths) {
            System.out.println(path);
            System.out.format("Uploading %s to S3 bucket %s...\n", path, bucketName);
            upload(client,path,bucketName,filePath);
        }
    }
    public static void main(String[] args) {
             local2s3(bucketName,filePath+"/");
    }
}
