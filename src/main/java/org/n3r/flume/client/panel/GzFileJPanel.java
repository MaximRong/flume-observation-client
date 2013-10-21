package org.n3r.flume.client.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.n3r.flume.client.common.FlumeClientConstant;

import com.google.common.io.Resources;

public class GzFileJPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4722361898973624437L;
	private JLabel image;
	private JLabel zipFileNameLabel;
	private JLabel downloadLabel;
	private JLabel fileDateLabel;
	private JLabel fileVersionLabel;

	private final String fileShortName;
	private final String fileDate;
	private final String fileVersion;
	private final String fileAllName;

	public GzFileJPanel(String fileAllName) {
		String[] split = StringUtils.split(fileAllName, '.');
		this.fileAllName = fileAllName;
		this.fileShortName = split[0];
		this.fileDate = split[1];
		this.fileVersion = split[2];
		initialize();
	}

	private void initialize() {
		setPreferredSize(new Dimension(100, 60));
		setBackground(new Color(182, 194, 154));
		setBorder(new MatteBorder(1, 1, 1, 1, new Color(138, 151, 125)));
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.3;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		image = new JLabel(
				new ImageIcon(Resources
						.getResource("images/gnome-mime-application-x-zip.png")),
				JLabel.CENTER);
		add(image, c);

		c.gridx = 1;
		c.weightx = 0.6;
		zipFileNameLabel = new JLabel(fileShortName, JLabel.LEFT);
		zipFileNameLabel.setForeground(Color.WHITE);
		zipFileNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
		zipFileNameLabel.setPreferredSize(new Dimension(10, 10));
		add(zipFileNameLabel, c);

		c.gridx = 2;
		c.weightx = 0.1;
		downloadLabel = new JLabel("下载", JLabel.CENTER);
		downloadLabel.setFont(new Font("楷体", Font.PLAIN, 14));
		downloadLabel.setForeground(new Color(0, 49, 79));
		add(downloadLabel, c);
		downloadLabel.addMouseListener(new GzFileDownLoadListener());

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 0.9;
		fileDateLabel = new JLabel("日期 : " + fileDate, JLabel.CENTER);
		fileDateLabel.setFont(new Font("楷体", Font.PLAIN, 12));
		fileDateLabel.setForeground(new Color(0, 49, 79));
		add(fileDateLabel, c);

		c.gridx = 2;
		c.weightx = 0.1;
		fileVersionLabel = new JLabel("版本 ： " + fileVersion, JLabel.CENTER);
		fileVersionLabel.setFont(new Font("楷体", Font.PLAIN, 12));
		fileVersionLabel.setForeground(new Color(0, 49, 79));
		add(fileVersionLabel, c);
	}

	class GzFileDownLoadListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel label = (JLabel) e.getComponent();

			// 选择保存位置
			JFileChooser fc = new JFileChooser();
			fc.setSelectedFile(new File(fileAllName));
			fc.showOpenDialog(label);
			final File file = fc.getSelectedFile();

			try {
				NioSocketConnector connector = new NioSocketConnector();
				DefaultIoFilterChainBuilder chain = connector.getFilterChain();
				connector.setHandler(new IoHandlerAdapter() {
					@Override
					public void messageReceived(IoSession session,
							Object message) throws Exception {
						FileChannel fc = null;
						FileOutputStream fileOutputStream = null;
						try {
							IoBuffer ib = (IoBuffer) message;
							fileOutputStream = new FileOutputStream(file);
							fc = fileOutputStream.getChannel();
							fc.write(ib.buf());
						} finally {
//							IOUtils.closeQuietly(fileOutputStream);
//							IOUtils.closeQuietly(fc);
//							session.close(true);
						}


					}
				});
				IoBuffer buffer = IoBuffer.allocate(40, false);
				try {
					buffer.putString(fileAllName, Charset.forName("UTF-8")
							.newEncoder());
				} catch (CharacterCodingException ex) {
					throw new RuntimeException(ex);
				}
				buffer.flip();

//				IoBuffer buffer = IoBuffer.allocate(12, false);
//				CharSequence cs = "msg";
//				buffer.putString(cs, Charset.forName("UTF-8").newEncoder());
//				buffer.flip();
				ConnectFuture connectFuture = connector
						.connect(new InetSocketAddress(
								FlumeClientConstant.InetSocketAddress, FlumeClientConstant.InetSocketFilePort));

				connectFuture.awaitUninterruptibly();
				IoSession session = connectFuture.getSession();
				session.write(buffer);

				session.getCloseFuture().awaitUninterruptibly();
				connector.dispose(true);

			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

			/*
			FlumeConnector connector = new FlumeConnector()
					.newFileDownloadConnector()
					.inetSocketAddress(
							new InetSocketAddress(
									FlumeClientConstant.InetSocketAddress, 3333))
					.handler(new IoHandlerAdapter() {

						@Override
						public void messageReceived(IoSession session,
								Object message) throws Exception {
							FileChannel fc = null;
							FileOutputStream fileOutputStream = null;
							try {
								IoBuffer ib = (IoBuffer) message;
								fileOutputStream = new FileOutputStream(file);
								fc = fileOutputStream.getChannel();
								fc.write(ib.buf());
							} finally {
								IOUtils.closeQuietly(fileOutputStream);
								IOUtils.closeQuietly(fc);
							}

							System.out.println("receive");
							IoBuffer ib = (IoBuffer) message;
							FileChannel fc = null;
							if (fc == null) {
								fc = new FileOutputStream(
										"e:/TEMP2/error_copy.2013-10-10.0.log")
										.getChannel();
							}
							fc.write(ib.buf());
							session.close(true);
						}
					}).build();

			IoBuffer buffer = IoBuffer.allocate(40, false);
			try {
				buffer.putString(fileAllName, Charset.forName("UTF-8")
						.newEncoder());
			} catch (CharacterCodingException ex) {
				throw new RuntimeException(ex);
			}
			buffer.flip();

			connector.sendMessage(buffer);
			*/

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			component.setCursor(FlumeClientConstant.HAND_CURSOR);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			component.setCursor(FlumeClientConstant.DEFAULT_CURSOR);
		}

	}

	class FileReceiveHandler extends IoHandlerAdapter {

		@Override
		public void messageReceived(IoSession session, Object message)
				throws Exception {
			System.out.println("receive");
			IoBuffer ib = (IoBuffer) message;
			FileChannel fc = null;
			if (fc == null) {
				fc = new FileOutputStream(
						"e:/TEMP2/error_copy.2013-10-10.0.log").getChannel();
			}
			fc.write(ib.buf());
			session.close(true);
			fc.close();
		}

	}
}
