package net.sf.seesea.lib.ui;

import net.sf.seesea.lib.IFeedbackMessageConsumer;
import net.sf.seesea.lib.Messages;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class DefaultFeedbackMessageConsumer implements IFeedbackMessageConsumer {

	@Override
	public void noProviderAvailable() {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.getString("ThreadedSerialInputReader.noProvider"), Messages.getString("ThreadedSerialInputReader.invalidStream")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	@Override
	public void processingStopped() {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.getString("ThreadedSerialInputReader.processingStopped"), Messages.getString("ThreadedSerialInputReader.stop1")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	@Override
	public void timeout() {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageDialog.openError(Display.getDefault().getActiveShell(), Messages.getString("ThreadedSerialInputReader.timeout"), Messages.getString("ThreadedSerialInputReader.nodata")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

}
