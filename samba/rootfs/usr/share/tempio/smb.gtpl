[global]
   netbios name = {{ env "HOSTNAME" }}
   workgroup = {{ .workgroup }}
   server string = Samba Home Assistant

   security = user
   ntlm auth = yes

   load printers = no
   disable spoolss = yes

   log level = 1

   bind interfaces only = yes
   interfaces = 127.0.0.1 {{ .interfaces | join " " }}
   hosts allow = 127.0.0.1 {{ .allow_hosts | join " " }}

   {{ if .compatibility_mode }}
   client min protocol = NT1
   server min protocol = NT1
   {{ end }}

   mangled names = no
   dos charset = CP850
   unix charset = UTF-8

{{- $vf := .veto_files -}}
{{- $u := .users }}
{{ range $s := .shares }}
[{{- regexReplaceAll "[^A-Za-z0-9_/ ]" .share "_" | regexFind "[A-Za-z0-9_ ]+$"}}]
   browseable = yes
   writeable = yes
   path = /{{-  regexReplaceAll "^config" .share "homeassistant" }}
   valid users = {{if or .users .ro_users -}}
                     {{- .users | join " " }} {{ .ro_users | join " " -}}
		 {{- else -}}
                     {{- range $user := $u }}{{ $user.username }} {{ end -}}
		 {{- end }}
   {{ if .ro_users -}}
     read list = {{ .ro_users | join " " }}
   {{- end }}
   force user = root
   force group = root
   veto files = /{{ $vf | join "/" }}/
   delete veto files = {{ eq (len $vf) 0 | ternary "no" "yes" }}
{{ end }}
