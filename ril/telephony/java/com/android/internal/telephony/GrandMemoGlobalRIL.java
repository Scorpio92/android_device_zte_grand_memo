//original RIL class from CM10.1
package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Parcel;
import android.os.Registrant;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GrandMemoGlobalRIL extends RIL
  implements CommandsInterface
{
  private static final int RIL_UNSOL_OEM_HOOK_RAW = 1028;
  private static final int RIL_UNSOL_TETHERED_MODE_STATE_CHANGED = 1037;
  private boolean mIsSendingSMS = false;
  private Object mSMSLock = new Object();

  public GrandMemoGlobalRIL(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext, paramInt1, paramInt2);
  }

  private void constructCdmaSendSmsRilRequest(RILRequest paramRILRequest, byte[] paramArrayOfByte)
  {
    DataInputStream localDataInputStream = new DataInputStream(new ByteArrayInputStream(paramArrayOfByte));
    try
    {
      paramRILRequest.mp.writeInt(localDataInputStream.readInt());
      paramRILRequest.mp.writeByte((byte)localDataInputStream.readInt());
      paramRILRequest.mp.writeInt(localDataInputStream.readInt());
      paramRILRequest.mp.writeInt(localDataInputStream.read());
      paramRILRequest.mp.writeInt(localDataInputStream.read());
      paramRILRequest.mp.writeInt(localDataInputStream.read());
      paramRILRequest.mp.writeInt(localDataInputStream.read());
      byte b1 = (byte)localDataInputStream.read();
      paramRILRequest.mp.writeByte(b1);
      for (byte b2 = 0; b2 < b1; b2 = (byte)(b2 + 1))
        paramRILRequest.mp.writeByte(localDataInputStream.readByte());
      paramRILRequest.mp.writeInt(localDataInputStream.read());
      paramRILRequest.mp.writeByte((byte)localDataInputStream.read());
      byte b3 = (byte)localDataInputStream.read();
      paramRILRequest.mp.writeByte(b3);
      for (byte b4 = 0; b4 < b3; b4 = (byte)(b4 + 1))
        paramRILRequest.mp.writeByte(localDataInputStream.readByte());
      int i = localDataInputStream.read();
      paramRILRequest.mp.writeInt(i);
      for (int j = 0; j < i; j++)
        paramRILRequest.mp.writeByte(localDataInputStream.readByte());
    }
    catch (IOException localIOException)
    {
      riljLog("sendSmsCdma: conversion from input stream to object failed: " + localIOException);
    }
  }

  private boolean isQcUnsolOemHookResp(ByteBuffer paramByteBuffer)
  {
    int i = 8 + "QUALCOMM".length();
    if (paramByteBuffer.capacity() < i)
      Log.d("RILJ", "RIL_UNSOL_OEM_HOOK_RAW data size is " + paramByteBuffer.capacity());
    String str;
    do
    {
      return false;
      byte[] arrayOfByte = new byte["QUALCOMM".length()];
      paramByteBuffer.get(arrayOfByte);
      str = new String(arrayOfByte);
      Log.d("RILJ", "Oem ID in RIL_UNSOL_OEM_HOOK_RAW is " + str);
    }
    while (!str.equals("QUALCOMM"));
    return true;
  }

  private void processUnsolOemhookResponse(ByteBuffer paramByteBuffer)
  {
    int i = paramByteBuffer.getInt();
    Log.d("RILJ", "Response ID in RIL_UNSOL_OEM_HOOK_RAW is " + i);
    int j = paramByteBuffer.getInt();
    if (j < 0)
    {
      Log.e("RILJ", "Response Size is Invalid " + j);
      return;
    }
    paramByteBuffer.get(new byte[j], 0, j);
    switch (i)
    {
    default:
      Log.d("RILJ", "Response ID " + i + "is not served in this process.");
      Log.d("RILJ", "To broadcast an Intent via the notifier to external apps");
      return;
    case 525289:
      Log.d("RILJ", "notifyCdmaFwdBurstDtmf(response_data)");
      return;
    case 525290:
      Log.d("RILJ", "notifyCdmaFwdContDtmfStart(response_data)");
      return;
    case 525291:
      Log.d("RILJ", "notifyCdmaFwdContDtmfStop()");
      return;
    case 525292:
    }
    Log.d("RILJ", "notifyCallReestablish()");
  }

  protected void processUnsolicited(Parcel paramParcel)
  {
    int i = paramParcel.dataPosition();
    int j = paramParcel.readInt();
    switch (j)
    {
    default:
      paramParcel.setDataPosition(i);
      super.processUnsolicited(paramParcel);
    case 1028:
    case 1037:
    }
    Object localObject;
    do
    {
      return;
      localObject = responseRaw(paramParcel);
      while (true)
        switch (j)
        {
        default:
          return;
        case 1028:
          unsljLogvRet(j, IccUtils.bytesToHexString((byte[])(byte[])localObject));
          ByteBuffer localByteBuffer = ByteBuffer.wrap((byte[])(byte[])localObject);
          localByteBuffer.order(ByteOrder.nativeOrder());
          if (isQcUnsolOemHookResp(localByteBuffer))
          {
            Log.d("RILJ", "OEM ID check Passed");
            processUnsolOemhookResponse(localByteBuffer);
            return;
            localObject = responseInts(paramParcel);
          }
          break;
        case 1037:
          Log.d("RILJ", "RIL_UNSOL_TETHERED_MODE_STATE_CHANGED Ignore");
          return;
        }
    }
    while (this.mUnsolOemHookRawRegistrant == null);
    Log.d("RILJ", "External OEM message, to be notified");
    this.mUnsolOemHookRawRegistrant.notifyRegistrant(new AsyncResult(null, localObject, null));
  }

  protected Object responseIccCardStatus(Parcel paramParcel)
  {
    IccCardStatus localIccCardStatus = new IccCardStatus();
    localIccCardStatus.setCardState(paramParcel.readInt());
    localIccCardStatus.setUniversalPinState(paramParcel.readInt());
    localIccCardStatus.mGsmUmtsSubscriptionAppIndex = paramParcel.readInt();
    localIccCardStatus.mCdmaSubscriptionAppIndex = paramParcel.readInt();
    localIccCardStatus.mImsSubscriptionAppIndex = paramParcel.readInt();
    int i = paramParcel.readInt();
    if (i > 8)
      i = 8;
    localIccCardStatus.mApplications = new IccCardApplicationStatus[i];
    for (int j = 0; j < i; j++)
    {
      IccCardApplicationStatus localIccCardApplicationStatus = new IccCardApplicationStatus();
      localIccCardApplicationStatus.app_type = localIccCardApplicationStatus.AppTypeFromRILInt(paramParcel.readInt());
      localIccCardApplicationStatus.app_state = localIccCardApplicationStatus.AppStateFromRILInt(paramParcel.readInt());
      localIccCardApplicationStatus.perso_substate = localIccCardApplicationStatus.PersoSubstateFromRILInt(paramParcel.readInt());
      localIccCardApplicationStatus.aid = paramParcel.readString();
      localIccCardApplicationStatus.app_label = paramParcel.readString();
      localIccCardApplicationStatus.pin1_replaced = paramParcel.readInt();
      localIccCardApplicationStatus.pin1 = localIccCardApplicationStatus.PinStateFromRILInt(paramParcel.readInt());
      localIccCardApplicationStatus.pin2 = localIccCardApplicationStatus.PinStateFromRILInt(paramParcel.readInt());
      paramParcel.readByte();
      paramParcel.readByte();
      paramParcel.readByte();
      paramParcel.readByte();
      localIccCardStatus.mApplications[j] = localIccCardApplicationStatus;
    }
    return localIccCardStatus;
  }

  protected Object responseSMS(Parcel paramParcel)
  {
    synchronized (this.mSMSLock)
    {
      this.mIsSendingSMS = false;
      this.mSMSLock.notify();
      return super.responseSMS(paramParcel);
    }
  }

  public void sendCdmaSms(byte[] paramArrayOfByte, Message paramMessage)
  {
    RILRequest localRILRequest = RILRequest.obtain(87, paramMessage);
    constructCdmaSendSmsRilRequest(localRILRequest, paramArrayOfByte);
    riljLog(localRILRequest.serialString() + "> " + requestToString(localRILRequest.mRequest));
    send(localRILRequest);
  }

  public void setPreferredNetworkType(int paramInt, Message paramMessage)
  {
    RILRequest localRILRequest = RILRequest.obtain(73, paramMessage);
    localRILRequest.mp.writeInt(1);
    localRILRequest.mp.writeInt(paramInt);
    this.mSetPreferredNetworkType = paramInt;
    this.mPreferredNetworkType = paramInt;
    riljLog(localRILRequest.serialString() + "> " + requestToString(localRILRequest.mRequest) + " : " + paramInt);
    send(localRILRequest);
  }
}

/* Location:           C:\Apk1.5\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.internal.telephony.GrandMemoGlobalRIL
 * JD-Core Version:    0.6.0
 */
