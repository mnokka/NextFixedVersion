// from https://community.atlassian.com/t5/Marketplace-Apps-questions/Automatically-set-next-unreleased-fixVersion-for-new-issues-or/qaq-p/274207

import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger
import org.apache.log4j.Level

import com.atlassian.jira.issue.Issue
import com.atlassian.jira.issue.MutableIssue



def versionManager = ComponentAccessor.getVersionManager()
def projectManager = ComponentAccessor.getProjectManager()
def project = projectManager.getProjectObjByKey(issue.projectObject.key)



def versions = versionManager.getVersions(project)

def newversions = versions.collect()
  
def log = Logger.getLogger("FindNextVersion")
log.setLevel(Level.INFO) // DEBUG INFO
  
log.info("---------- FindNextVersion started ---------------------------------------")


def Project=issue.projectObject.name
//versions.sort {it.releaseDate}
newversions = newversions.sort({version1, version2 -> version1.releaseDate<=>version2.releaseDate}).findAll{version -> ! version.released }



if (newversions) {
	log.debug("First element: " + newversions.first())
	log.debug("All elements: " + newversions)
	
	def versionToUse = newversions.first();
	
	
	MutableIssue myIssue = issue
	myIssue.setFixVersions([versionToUse])
	myIssue.store() // needed to store changes
	log.info("Set version:${versionToUse} as fixed version for issue:${issue}")
}

else {
	
	log.error("Project:${Project} ==> ERROR: No open versions found. Cannot set issue:${issue} fixed version")
}





log.info("---------- FindNextVersion stopped ---------------------------------------")