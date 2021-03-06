/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package net.sf.seesea.rendering.chart;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.swt.events.GestureEvent;
import org.eclipse.ui.IEditorPart;

public class GestureEditDomain extends DefaultEditDomain implements IViewerGestureListener {

	public GestureEditDomain(IEditorPart editorPart) {
		super(editorPart);
	}

	public void gesturePerformed(GestureEvent event, EditPartViewer viewer) {
		Tool tool = getActiveTool();
		if (tool instanceof IViewerGestureListener)
			((IViewerGestureListener) tool).gesturePerformed(event, viewer);
	}

	// super.defaultTool is private
	private Tool defaultTool;

	@Override
	public Tool getDefaultTool() {
		if (defaultTool == null) {
			defaultTool = new GestureSelectionTool();
			super.setDefaultTool(defaultTool);
		}
		return defaultTool;
	}
}
