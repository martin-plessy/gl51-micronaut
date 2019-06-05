ruleset {
	DeadCode
	UnusedImport
	UnusedVariable {
		ignoreVariableNames = "_*"
	}

	JUnitUnnecessarySetUp
	JUnitUnnecessaryTearDown

	PackageName
	ClassName

	CyclomaticComplexity {
		maxMethodComplexity = 2
	}
}
