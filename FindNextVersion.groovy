// Idea from https://community.atlassian.com/t5/Marketplace-Apps-questions/Automatically-set-next-unreleased-fixVersion-for-new-issues-or/qaq-p/274207

// Modifications:
// Added setting Issue fixVersion to next available (time) version
// To be used as Jira Script Runner Behaviours / Initializer  (version is set when issue created. In the Create Issue screen)
// (can be used as postfuntion, but remove package declarion and install to normal Jira scripts directory)
// Script Runner examples made this possible!
//
// Dec 2017    mika.nokka1@gmail.com   


package com.onresolve.jira.groovy.doit2  // this script must be living under this tree starting from /scripts directory (<jira place>/scripts/com/resolve....)
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import org.apache.log4j.Level
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue
import com.onresolve.jira.groovy.user.FieldBehaviours   // class to be used if script in server
import com.atlassian.jira.issue.IssueManager
import static com.atlassian.jira.issue.IssueFieldConstants.*  // can use things like AFFECTED_VERSIONS see: https://docs.atlassian.com/software/jira/docs/api/7.6.1/constant-values.html#com.atlassian.jira.issue.IssueFieldConstants.FIX_FOR_VERSIONS

public class FindNextVersions extends FieldBehaviours {

void Doit2() {	// just a method runtime system is calling (used in Behaviours configurations)
	
		def log = Logger.getLogger("FindNextVersion")
		log.setLevel(Level.INFO) // DEBUG INFO
	
		log.debug("---------- FindNextVersion started ---------------------------------------")
		def issue="NOT_EXISTS"
		issue=underlyingIssue // from ScriptRunner example code		
		def versionManager = ComponentAccessor.getVersionManager()
		def projectManager = ComponentAccessor.getProjectManager()
		def project = projectManager.getProjectObjByKey(issueContext.projectObject.key)
		def versions = versionManager.getVersions(project)
		//def issueManager = ComponentAccessor.getIssueManager()
		def newversions = versions.collect()
		newversions = newversions.sort({version1, version2 -> version1.releaseDate<=>version2.releaseDate}).findAll{version -> ! version.released }

		action= getActionName()
		log.debug("action:${action}")

		if (action == "Create") {
			if (newversions) {
				log.debug("First element: " + newversions.first())
				log.debug("All elements: " + newversions)
	
				def versionToUse = newversions.first();
				getFieldById(FIX_FOR_VERSIONS).setFormValue([versionToUse.id])  // from Script Runner examples, just used fixed versions thing
				log.info("Set version:${versionToUse} as fixed version for current Create issue screen")
			}

			else {
				log.error("${project} ==> ERROR: No open versions found. Cannot set Create issue screen fixed version")
				return
			}
		}

		else {
			log.debug("Not Create action. Maybe existing issue:${issue}. NOT CHANGING ANYTHING")
			return
		}


		log.debug("---------- FindNextVersion stopped ---------------------------------------")
	}
}