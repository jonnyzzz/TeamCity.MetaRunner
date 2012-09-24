package jetbrains.buildserver.metarunner.proxy

import jetbrains.buildserver.metarunner.specs.SpecsWatcher

/**
 * @author Eugene Petrenko (eugene.petrenko@jetbrains.com)
 * 13.12.10 23:13 
 */

class SpecsRefresher(locator : UpdatableSpecsLocator, watcher : SpecsWatcher) {
  watcher.addFilesChangedListener(new Runnable{
    def run() {
      locator.reloadRenners()
    }
  })
}
