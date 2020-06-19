package com.github.sarxos.webcam.ds.civil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.lti.civil.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.WebcamDevice;
import com.lti.civil.awt.AWTImageConverter;


/**
 * Webcam device - LTI-CIVIL framework compatible implementation.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class LtiCivilDevice implements WebcamDevice, CaptureObserver, WebcamDevice.FPSSource {

	private static final Logger LOG = LoggerFactory.getLogger(LtiCivilDevice.class);

	private CaptureDeviceInfo cdi = null;
	private List<Dimension> dimensions = null;
	private Dimension size = null;
	private Image image = null;
	private CaptureStream stream = null;

	private AtomicBoolean open = new AtomicBoolean(false);

	private volatile boolean capturing = false;
	private volatile boolean disposed = false;

	private long t1 = -1;
	private long t2 = -1;

	private volatile double fps = 0;

	protected LtiCivilDevice(CaptureDeviceInfo cdi) {
		this.cdi = cdi;
	}

	@Override
	public String getName() {
		return cdi.getDescription();
	}

	@Override
	public Dimension[] getResolutions() {
		System.out.println("GETTING RESOLUTIONSSS");

		if (dimensions == null) {
			dimensions = new ArrayList<Dimension>();
			System.out.println("SET1");

//			CaptureSystem system = LtiCivilDriver.getCaptureSystem();

			CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
			CaptureSystem system = null;

			try {
				system = factory.createCaptureSystem();
			} catch (CaptureException e) {
				e.printStackTrace();
			}

			try {
				system.init();
			} catch (CaptureException e) {
				e.printStackTrace();
			}

			System.out.println("CRASH?");

			Set<Dimension> set = new HashSet<Dimension>();

			try {
				System.out.println("Opening captureStream");
				System.out.println(cdi.getDeviceID());

//				stream = system.openCaptureDeviceStream(cdi.getDeviceID());
				CaptureStream stream = system.openCaptureDeviceStream(cdi.getDeviceID());
				System.out.println("GET RES, ENUM FORMATS");

				for (VideoFormat format : stream.enumVideoFormats()) {
					System.out.println("ENUMING");

					if (format.getFormatType() == VideoFormat.RGB24) {
						set.add(new Dimension(format.getWidth(), format.getHeight()));
					}
				}

				stream.dispose();

			} catch (CaptureException e) {
				LOG.error("Capture exception when collecting formats dimension", e);
			}

			dimensions.addAll(set);

			Collections.sort(dimensions, new Comparator<Dimension>() {

				@Override
				public int compare(Dimension a, Dimension b) {
					int apx = a.width * a.height;
					int bpx = b.width * b.height;
					if (apx > bpx) {
						return 1;
					} else if (apx < bpx) {
						return -1;
					} else {
						return 0;
					}
				}
			});
		}

		return dimensions.toArray(new Dimension[dimensions.size()]);
	}

	@Override
	public BufferedImage getImage() {
		if (!capturing) {
			return null;
		}
		return AWTImageConverter.toBufferedImage(image);
	}

	@Override
	public void onError(CaptureStream stream, CaptureException e) {
		LOG.error("Exception in capture stream", e);
	}

	@Override
	public void onNewImage(CaptureStream stream, Image image) {

		if (t1 == -1 || t2 == -1) {
			t1 = System.currentTimeMillis();
			t2 = System.currentTimeMillis();
		}

		this.image = image;
		this.capturing = true;

		t1 = t2;
		t2 = System.currentTimeMillis();

		fps = (4 * fps + 1000 / (t2 - t1 + 1)) / 5;
	}

	@Override
	public void open() {
		System.out.println("OPENING DEVICE");

		if (disposed) {
			return;
		}

		if (open.compareAndSet(false, true)) {

			try {
				stream = LtiCivilDriver.getCaptureSystem().openCaptureDeviceStream(cdi.getDeviceID());
				stream.setVideoFormat(findFormat());
				stream.setObserver(this);
				stream.start();
			} catch (CaptureException e) {
				LOG.error("Capture exception when opening Civil device", e);
			}
		}

		while (true) {
			if (capturing) {
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	private VideoFormat findFormat() {
		if (stream == null) {
			throw new RuntimeException("Stream is null");
		}
		if (size == null) {
			throw new RuntimeException("Size is not set");
		}
		try {
			int count = 0;
			for (VideoFormat format : stream.enumVideoFormats()) {
				if (count == 8) {
					System.out.println("RETURNING FORMAT 8");
					return format;
				}
				++count;

			}
		} catch (CaptureException e) {
			LOG.error("Capture exception when iterating thru video formats", e);
		}
		throw new RuntimeException("Cannot find RGB24 video format for size [" + size.width + "x" + size.height + "]");
	}

	@Override
	public void close() {
		if (open.compareAndSet(true, false)) {
			try {
				stream.stop();
				stream.dispose();
			} catch (CaptureException e) {
				LOG.error("Capture exception when closing Civil device", e);
			}
		}
	}

	@Override
	public Dimension getResolution() {
		System.out.println("GETTING RESOLUTION");

		return size;
	}

	@Override
	public void setResolution(Dimension d) {
		System.out.println("SETTING RESOLUTION");
		this.size = d;
	}

	@Override
	public void dispose() {
		System.out.println("DISPOSING");

		disposed = true;
	}

	@Override
	public boolean isOpen() {

		return open.get();
	}

	@Override
	public double getFPS() {

		return fps;
	}
}




