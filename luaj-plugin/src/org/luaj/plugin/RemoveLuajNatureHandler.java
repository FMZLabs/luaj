package org.luaj.plugin;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.luaj.plugin.builder.LuajNature;

public class RemoveLuajNatureHandler  implements IObjectActionDelegate {

	private ISelection selection;
	
	/**
	 * Constructor for AddLuajNatureToIProjectAction.
	 */
	public RemoveLuajNatureHandler() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
				if (project != null) {
					try {
						project.setPersistentProperty(LuajNature.HAS_LUAJ_NATURE, null);
						removeLuajNature(project);
						System.out.println("Luaj nature removed from "+project);
					} catch (CoreException e) {
						//TODO log something
						throw new RuntimeException("Failed to remove luaj nature",
								e);
					}
				}
			}
		}
	}

	/**
	 * Removes luaj nature from a project
	 *
	 * @param project
	 *            to have sample nature removed
	 */
	private void removeLuajNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();

		for (int i = 0; i < natures.length; ++i) {
			if (LuajNature.NATURE_ID.equals(natures[i])) {
				System.out.println("Removing nature id "+LuajNature.NATURE_ID);
				// Remove the nature
				String[] newNatures = new String[natures.length - 1];
				System.arraycopy(natures, 0, newNatures, 0, i);
				System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
				description.setNatureIds(newNatures);
				project.setDescription(description, null);
				return;
			}
		}
	}


}
