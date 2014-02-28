/*
	This file is part of the javaappplatform library.
	Copyright (C) 2011-2013 Hendrik Renken

	This library is subject to the terms of the Mozilla Public License, v. 2.0.
	You should have received a copy of the MPL along with this library; see the
	file LICENSE. If not, you can obtain one at http://mozilla.org/MPL/2.0/.
*/
package github.javaappplatform.resources.loader;

import github.javaappplatform.commons.util.Close;
import github.javaappplatform.resources.IResource;
import github.javaappplatform.resources.Stream;
import github.javaappplatform.resources.internal.ILoader;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;

/**
 * TODO javadoc
 * @author funsheep
 */
public class BufferedImageLoader implements ILoader
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object load(IResource resource, String returntype, Map<String, Object> properties) throws IOException
	{
		ImageReader reader = null;
		try
		{
			final Iterator<ImageReader> i = ImageIO.getImageReadersByMIMEType(resource.mimetype());
			if (!i.hasNext())
				throw new IOException("Could not find appropriate image parser for mimetype: " + resource.mimetype());
			reader = i.next();
			ImageReadParam param = reader.getDefaultReadParam();
			reader.setInput(ImageIO.createImageInputStream(Stream.open(resource).toRead()), true, true);
			BufferedImage img = reader.read(0, param);
			if (Raster.class.getName().equals(returntype))
				return img.getAlphaRaster();
			return img;
		}
		finally
		{
			Close.close(reader);
		}
	}

}
