To find next available unreleased Jira Fixed version (this can be set be be as default fixedVersion field value)

Original reference: https://community.atlassian.com/t5/Marketplace-Apps-questions/Automatically-set-next-unreleased-fixVersion-for-new-issues-or/qaq-p/274207

Script Runner used in implementation
Special thanks to tomi.kallio@ambientia.fi

1) Master branch: Postfunction implementation. Find next available (not released) version and set it as fixed version. 

2) Initializer branch: Behaviours Initializer implementation. Find next available (not released) version and set it as fixed version in Create Screen
