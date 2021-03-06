/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.luaj.plugin;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.luaj.plugin.builder.LuajNature;
import org.osgi.framework.Bundle;

/**
 * An example showing how to control when an element is decorated. This example
 * decorates only elements that are instances of IResource and whose attribute
 * is 'Read-only'.
 * 
 * @see ILightweightLabelDecorator
 */
public class DecorateHasLuajNature implements ILightweightLabelDecorator {
	/**
	 * String constants for the various icon placement options from the template
	 * wizard.
	 */
	public static final String TOP_RIGHT = "TOP_RIGHT";

	public static final String TOP_LEFT = "TOP_LEFT";

	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";

	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";

	public static final String UNDERLAY = "UNDERLAY";

	/** The integer value representing the placement options */
	private int quadrant;

	/** The icon image location in the project folder */
	private String iconPath = "icons/luajproject.gif"; //NON-NLS-1

	/**
	 * The image description used in
	 * <code>addOverlay(ImageDescriptor, int)</code>
	 */
	private ImageDescriptor descriptor;

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object, org.eclipse.jface.viewers.IDecoration)
	 */
	public void decorate(Object element, IDecoration decoration) {
		/**
		 * Checks that the element is an IResource with the 'Read-only' attribute
		 * and adds the decorator based on the specified image description and the
		 * integer representation of the placement option.
		 */
		IResource resource = (IResource) element;
		if (!(resource instanceof IProject)) return;
		IProject project = (IProject) resource;
		try {
			if (hasLuajNature(project)) {
				Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
				URL url = FileLocator.find(
						bundle, new Path(iconPath), null); //NON-NLS-1

				if (url == null)
					return;
				descriptor = ImageDescriptor.createFromURL(url);			
				quadrant = IDecoration.TOP_LEFT;
				decoration.addOverlay(descriptor,quadrant);
			} else {
				// TODO: refresh the project view.
			}
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
	}

	private boolean hasLuajNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();

		for (int i = 0; i < natures.length; ++i) {
			if (LuajNature.NATURE_ID.equals(natures[i])) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}
}