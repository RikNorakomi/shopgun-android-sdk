package com.eTilbudsavis.etasdk;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.os.Environment;

import com.eTilbudsavis.etasdk.log.EtaLog;
import com.eTilbudsavis.etasdk.model.Session;
import com.eTilbudsavis.etasdk.utils.PermissionUtils;

public class ExternalClientIdStore {
	
	public static final String TAG = ExternalClientIdStore.class.getSimpleName();
	
	public static void updateCid(Session s, Context c) {
		
		String extCid = getCid(c);
		
		if (s.getClientId() == null) {
			
			s.setClientId("randomjunkid");
			
		} else if (!s.getClientId().equals(extCid)) {
			
			extCid = s.getClientId();
			saveCid(extCid, c);
			
		}
		
	}
	
	private static void saveCid(String cid, Context c) {
		
		File f = getCidFile(c);
		if (f==null) {
			return;
		}
		
		FileOutputStream fos = null;
		if (f.exists()) {
			f.delete();
		}
		try {
			fos = new FileOutputStream(f);
			fos.write(cid.getBytes());
			fos.flush();
		} catch (IOException e) {
			// Ignoring
		} finally {
			try{
				fos.close();
			} catch(Throwable t) {
				
			}
		}
		
	}
	
	private static String getCid(Context c) {

		File cidFile = getCidFile(c);
		if (cidFile==null) {
			return null;
		}
		
        RandomAccessFile f = null;
        try {
        	f = new RandomAccessFile(cidFile, "r");
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                return null;
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return new String(data);
        } catch (Exception e) {
        	// Ignore
		} finally {
            try {
				f.close();
			} catch (Exception e) {
				// Ignore
			}
        }
	        
		return null;
    }
	
	private static boolean deleteCid(Context c) {
		File f = getCidFile(c);
		if ( f != null && f.exists() ) {
			return f.delete();
		}
		return true;
	}
	
	private static File getCidFile(Context context) {
		
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) &&
				PermissionUtils.hasWriteExternalStorage(context)) {
			
			File cacheDir = new File(Environment.getExternalStorageDirectory(), "cache");
			if (!cacheDir.exists() && !cacheDir.mkdirs()) {
				EtaLog.w(TAG, "External directory couldn't be created");
				return null;
			}
			
			String fileName = context.getPackageName() + ".txt";
			return new File(cacheDir, fileName);
		}
		
		return null;
	}

	public static void test(Context c) {
		
		long start = System.currentTimeMillis();
		
		// Just clearing the prefs file
		deleteCid(c);
		
		boolean didFail = false;

		String extCid = null;
		
		// no CID has been obtained yet
		Session s = new Session();
		updateCid(s, c);
		if (s.getClientId()!=null || extCid!=null) {
			EtaLog.d(TAG, "ERROR: ClientId has been set: Session:" + s.getClientId() + ", mCid:" + extCid);
			didFail = true;
		}
		
		String first = "fake_client_id";
		s.setClientId(first);
		updateCid(s, c);
		if (!first.equals(s.getClientId())) {
			EtaLog.d(TAG, "ERROR: ClientId changed: Session:" + s.getClientId() + ", mCid:" + extCid);
			didFail = true;
		}
		if (!s.getClientId().equals(extCid)) {
			EtaLog.d(TAG, "ERROR: ClientId mismstch: Session:" + s.getClientId() + ", mCid:" + extCid);
			didFail = true;
		}
		
		String second = "new_fake_client_id";
		s.setClientId(second);
		updateCid(s, c);
		if (!second.equals(s.getClientId()) || !s.getClientId().equals(extCid)) {
			EtaLog.d(TAG, "ERROR: ClientId mismstch: Session:" + s.getClientId() + ", mCid:" + extCid);
			didFail = true;
		}
		
		deleteCid(c);
		EtaLog.d(TAG, "Test: " + (didFail ? "failed":"succeded") + ", in " + (System.currentTimeMillis()-start));

	}
	
}