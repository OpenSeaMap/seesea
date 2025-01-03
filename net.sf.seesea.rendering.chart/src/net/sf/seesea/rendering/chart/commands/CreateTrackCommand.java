/**
 * 
 Copyright (c) 2010-2012, Jens K�bler All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of the <organization> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package net.sf.seesea.rendering.chart.commands;

import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Route;
import net.sf.seesea.model.core.geo.RoutingContainer;
import net.sf.seesea.model.core.geo.Track;
import net.sf.seesea.model.core.geo.TracksContainer;
import net.sf.seesea.model.core.geo.osm.World;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

/**
 * 
 */
public class CreateTrackCommand extends AbstractTransactionalCommand {

	private final World world;

	/**
	 * @param domain
	 * @param label
	 * @param affectedFiles
	 */
	public CreateTrackCommand(TransactionalEditingDomain domain, String label, World world) {
		super(domain, label, getWorkspaceFiles(world));
		this.world = world;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		TracksContainer routingContainer = world.getTracksContainer(); 
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		if(routingContainer == null) {
			routingContainer = geoFactory.createTracksContainer();
			world.setTracksContainer(routingContainer);
		}
		Track route = geoFactory.createTrack();
		routingContainer.getTracks().add(route);
		return CommandResult.newOKCommandResult(route);
		
//		ConnectionCommand command = (ConnectionCommand)request.getStartCommand();
//		command.setTarget(getLogicSubpart());
//		ConnectionAnchor ctor = getLogicEditPart().getTargetConnectionAnchor(request);
//		if (ctor == null)
//			return null;
//		command.setTargetTerminal(getLogicEditPart().mapConnectionAnchorToTerminal(ctor));
//
//		
//		// TODO Auto-generated method stub
//		return null;
	}

}
