INSERT INTO Folder (persistentIdentifier, active, childrenCount, childrenState,
  dateAdded, dateModified,
  effectiveActive, name,
  numberOfAvailableTasks, numberOfContainedTasks, numberOfDueSoonTasks, numberOfOverdueTasks, numberOfRemainingTasks,
  rank)
  VALUES ('TEST-FOLDER', 0, 25, 0,
  strftime('%s', '2007-03-15') - strftime('%s', '2001-01-01'), strftime('%s', '2015-05-04') - strftime('%s', '2001-01-01'),
  0, 'Folder Test',
  34, 27, 119, 145, 121,
  19);
INSERT INTO Task (persistentIdentifier, blocked, blockedByFutureStartDate,
  childrenCount, childrenCountAvailable, childrenCountCompleted, childrenState, completeWhenChildrenComplete,
  containingProjectContainsSingletons, containsNextTask, context,
  dateAdded, dateCompleted,
  dateDue, dateModified,
  effectiveContainingProjectInfoActive, effectiveContainingProjectInfoRemaining,
  effectiveDateDue, effectiveDateToStart,
  effectiveFlagged, effectiveInInbox, estimatedMinutes, flagged,
  hasCompletedDescendant, hasFlaggedTaskInTree, hasUnestimatedLeafTaskInTree,
  inInbox, isDueSoon, isOverdue,
  name, parent, plainTextNote, projectInfo,
  rank, sequential)
  VALUES ('TEST-TASK-1', 0, 0,
  18, 22, 234, 0, 0,
  1, 2, 'TEST-CONTEXT-1',
  strftime('%s', '2007-03-15') - strftime('%s', '2001-01-01'), strftime('%s', '2015-05-04') - strftime('%s', '2001-01-01'),
  -1, -1,
  12, 18,
  strftime('%s', '2012-11-30') - strftime('%s', '2001-01-01'), strftime('%s', '1992-06-23') - strftime('%s', '2001-01-01'),
  1, 0, 2456, 1,
  0, 1, 1,
  1, 0, 1,
  'Task Test #1', 'TEST-PARENT-1', 'Test Note #1 Contents', 'TEST-PROJECT-INFO',
  45, 32),
('TEST-TASK-2', 1, 1,
  13, 76, 938, 0, 0,
  1, 2, 'TEST-CONTEXT-2',
  strftime('%s', '1980-11-11') - strftime('%s', '2001-01-01'), strftime('%s', '2034-08-30') - strftime('%s', '2001-01-01'),
  -1, -1,
  56, 14,
 NULL, NULL,
  0, 0, NULL, 1,
  0, 1, 1,
  0, 0, 1,
  NULL, NULL, NULL, 'TEST-PROJECT-INFO',
  97, 3),
  ('TEST-TASK-3', 1, 1,
  13, 76, 938, 0, 0,
  1, 2, 'TEST-CONTEXT-3',
  strftime('%s', '2222-12-22') - strftime('%s', '2001-01-01'), strftime('%s', '2016-04-03') - strftime('%s', '2001-01-01'),
  -1, -1,
  22, 14,
 NULL, NULL,
  0, 0, NULL, 1,
  0, 1, 1,
  0, 0, 1,
  'îøóöêå', NULL, 'いろはにほへとちりぬるを çà et là la qualité de son œuvre ’“”', 'TEST-PROJECT-INFO',
  97, 3);

INSERT INTO ProjectInfo (pk, containsSingletonActions, folder, folderEffectiveActive,
  numberOfAvailableTasks, numberOfContainedTasks, numberOfDueSoonTasks, numberOfOverdueTasks, numberOfRemainingTasks,
  status, task, taskBlocked, taskBlockedByFutureStartDate)
VALUES ('TEST-PROJECT-INFO', 1, 'TEST-FOLDER', 0,
  321, 87, 54, 22, 9,
  'TEST-STATUS', 'TEST-TASK', 0, 1);

INSERT INTO Context (persistentIdentifier, active, allowsNextAction, availableTaskCount,
  childrenCount, childrenState, containedTaskCount,
  dateAdded, dateModified,
  effectiveActive, localNumberOfDueSoonTasks,
  localNumberOfOverdueTasks, name, nextTaskCount, parent, rank,
  remainingTaskCount, totalNumberOfDueSoonTasks, totalNumberOfOverdueTasks)
VALUES ('TEST-CONTEXT-1', 1, 0, 25,
  455, 1, 213,
  strftime('%s', '2011-11-11') - strftime('%s', '2001-01-01'), strftime('%s', '2005-09-09') - strftime('%s', '2001-01-01'),
  342, 466,
  755, 'Test Context #1', 34, 'TEST-PARENT-1', 89,
  324, 908, 1014),
('TEST-CONTEXT-2', 0, 1, 25,
  22, 0, 432,
  strftime('%s', '2016-04-02') - strftime('%s', '2001-01-01'), strftime('%s', '2017-01-01') - strftime('%s', '2001-01-01'),
  54, 5,
  897, 'Test Context #2', 23, 'TEST-PARENT-2', 43,
  56, 77, 221);
