# Home Assistant Add-on: Samba share

## Installation

Follow these steps to get the add-on installed on your system:

1. Navigate in your Home Assistant frontend to **Settings** -> **Add-ons** -> **Add-on store**.
2. Find the "Samba share" add-on and click it.
3. Click on the "INSTALL" button.

## How to use

1. In the configuration section, setup at least one user.
   You can specify any usernames and passwords; these are not related in any way to the login credentials you use to log in to Home Assistant or to log in to the computer with which you will use Samba share.
2. Save the configuration.
3. Start the add-on.
4. Check the add-on log output to see the result.

## Connection

If you are on Windows you use `\\<IP_ADDRESS>\`, if you are on MacOS you use `smb://<IP_ADDRESS>` to connect to the shares.

By default this addon exposes the following directories over smb (samba):

Directory | Description
-- | --
`addons` | This is for your local add-ons.
`addon_configs` | This is for the configuration files of your add-ons.
`backup` | This is for your backups.
`config` | This is for your Home Assistant configuration.
`media` | This is for local media files.
`share` | This is for your data that is shared between add-ons and Home Assistant.
`ssl` | This is for your SSL certificates.

## Configuration

Add-on configuration:

```yaml
workgroup: WORKGROUP
users:
  - username: homeassistant
    password: qwerty
  - username: reader
    password: public
  - username: editor
    password: notepad
compatibility_mode: false
veto_files:
  - ._*
  - .DS_Store
  - Thumbs.db
  - icon?
  - .Trashes
allow_hosts:
  - 172.16.0.0/12
  - 192.168.0.0/16
  - fe80::/10
  - fc00::/7
shares:
  - share: addons
  - share: addon_configs
  - share: backup
  - share: config
  - share: media
  - share: share
    users:
      - editor
    ro_users:
      - reader
  - share: ssl
```

### Option: `workgroup` (required)

Change WORKGROUP to reflect your network needs.

### Option: `users` (required)

At least one user must be defined.

#### Sub-option: `username` (required)

The username you would like to use to authenticate with the Samba server.

#### Sub-option: `password` (required)

The password that goes with the username configured for authentication.

### Option: `shares` (required)

Any or all of `addons`, `addon_configs`, `backup`, `config`, `media`, `share`, `ssl`
or paths therein (e.g. `share/data` or [`config/www`](https://www.home-assistant.io/integrations/http/#hosting-files)).

#### Sub-option: `users` (optional)

List of users that have read-write access to the share.
If not specified, all defined users from the above will have access.

#### Sub-option: `ro_users` (optional)

List of users that have read-only access to the share.

### Option: `allow_hosts` (required)

List of hosts/networks allowed to access the shared folders.

### Option: `veto_files` (optional)

List of files that are neither visible nor accessible. Useful to stop clients
from littering the share with temporary hidden files
(e.g., macOS `.DS_Store` or Windows `Thumbs.db` files)

### Option: `compatibility_mode`

Setting this option to `true` will enable old legacy Samba protocols
on the Samba add-on. This might solve issues with some clients that cannot
handle the newer protocols, however, it lowers security. Only use this
when you absolutely need it and understand the possible consequences.

Defaults to `false`.
