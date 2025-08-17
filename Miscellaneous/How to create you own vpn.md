# Build Your Own VPN | [Free VPN – Detailed Video Summary](https://www.youtube.com/watch?v=6UIEtF-Hl2E)

## Introduction
**Creator:** Piyush Garg  
**Published:** June 15, 2024  
**Video:** "Build Your Own VPN | Free VPN"  
**Topics Covered:** VPN concept, OpenVPN, EC2 hosting, step-by-step setup, privacy benefits.

---

## What is a VPN and Why Use It?

- **VPN (Virtual Private Network)** creates a secure, encrypted tunnel between your device and a remote server.
- With a VPN, **your internet traffic is encrypted** and routed through the VPN server, hiding your real IP address.
- **Benefits:**
  - Privacy: Prevents your ISP and visited websites from tracking your location and browsing history.
  - Security: Protects data, especially on public networks.
  - Geo-unblocking: Lets you access region-restricted content by proxying requests from another country.

---

## Typical Internet vs. VPN Architecture

### Without VPN
- Your device connects **directly to the internet** via your ISP.
- **Your IP address is exposed** to every website you visit.
- Websites and advertisers can track your identity and location.
- You may face **geo-restrictions** on certain websites (e.g., some U.S. sites blocked in India).

### With VPN
- You install a **VPN client** on your device.
- The client encrypts your traffic and sends it to the **VPN server** (in your chosen region).
- The VPN server decrypts the data and accesses sites on your behalf — **the destination only sees the VPN server’s IP.**
- Provides privacy, bypasses geo-blocks, and hides your real location.

---

## Building Your Own Free VPN Server (OpenVPN + AWS EC2)

### Step 1: Pick a Cloud Region
- For geo-unblocking, select a cloud region **outside your home country**, e.g., Northern Virginia (US) or Singapore.

### Step 2: Launch an EC2 Instance
- Go to AWS EC2 dashboard, select "Launch Instance".
- Search AWS Marketplace for "**OpenVPN Access Server**" (community image).
- **Select the free tier for OpenVPN Access Server** (supports two free simultaneous connections).
- Choose an appropriate instance size (e.g., t2.large for better performance).

### Step 3: Configure Security Groups
- Allow necessary inbound traffic:
  - **SSH (port 22)** for admin access
  - **TCP ports 443 (VPN traffic)** and **943 (admin UI)**
- OpenVPN image will auto-set some security group rules.

### Step 4: SSH and Initial Server Setup
- Connect to the server using SSH and your key pair:
  ```
  ssh -i my-openvpn-key.pem openvpnas@<your-server-ip>
  ```
- On first login, complete guided setup:
  - Accept agreements
  - Configure interfaces (default is "all")
  - Admin UI port: 943
  - VPN TCP port: 443
  - Enable routing client traffic and DNS through VPN ("yes" for both)
  - Set **admin username ("openvpn")** and strong password

### Step 5: Access Admin Portal
- Visit: `https://<your-server-ip>:943/admin`
- Login with your chosen admin credentials.
- Review connected users and server settings.

### Step 6: Download VPN Client
- Go to: `https://<your-server-ip>:943/`
- Log in with your VPN user credentials.
- **Download the platform-specific OpenVPN client** — configuration is pre-embedded!
- Install and launch the client.

### Step 7: Connect and Verify
- Enter your VPN username/password in the client.
- **Connect — your public IP now reflects the EC2 region (e.g., USA instead of India).**
- Verify by using "What's My IP" and speed test sites.

### Step 8: Maintenance and Shutdown
- Track users and traffic via the admin portal.
- When finished, **terminate the EC2 instance** to stop billing and break the connection.

---

## Technical Architecture Diagram (textual)

```
[Your Device]
     |
[VPN Client] ---(encrypted)---> [VPN Server (EC2)]
                                     |
                             [Destination Websites]
```

- **VPN Client**: Encrypts your internet traffic.
- **VPN Server (EC2)**: Decrypts traffic and proxies requests.
- **Destination websites**: Only see the VPN server’s IP and location.

---

## Features and Limitations

- **Free OpenVPN license** allows two concurrent client connections.
- **Multiple regions possible**; simply repeat the setup in another EC2 region for alternate IPs.
- **Speed can drop** due to encryption and routing overhead.
- **Browser traffic, DNS requests, and all internet traffic** are routed securely via the VPN (if you choose full routing).
- **Docker images and multi-cloud documentation** are available for advanced deployments.

---

## Additional Resources

- **OpenVPN AWS Documentation:**  
  [openvpn.net/as-docs/aws-ec2.html]
- **OpenVPN Community Edition:**  
  Free for personal use on AWS, GCP, Azure, DigitalOcean, or on-prem servers.

---

## Key Takeaways

- Anyone can host their own private VPN server for minimal cost (just cloud infrastructure).
- **No monthly VPN fees**; total control and increased privacy.
- Highly useful for developers, privacy-conscious users, and bypassing geo-blocks.
- The process is suitable for beginners, with most steps automated or guided.

---

## Note
- This is just a configuration video and a detailed guide no how to create an end to end service.
- We only provide the servers for the service(like EC2). Other than that the encryption of packets and their transmission is all handled by service like openvpn.