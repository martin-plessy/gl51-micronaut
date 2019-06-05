ruleset {
	DeadCode
	UnusedImport
	UnusedVariable {
		ignoreVariableNames = "_*"
	}

	PackageName
	ClassName
	MethodName

	CyclomaticComplexity {
		maxMethodComplexity = 2
	}
}
