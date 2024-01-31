create-abc-client-files:
	for i in {1..60}; do \
		mkfile 100k "collector/sample-file-resources/origin/abc/layout01/ABC-$$i.gz"; \
		echo "file collector/sample-file-resources/origin/abc/layout01/ABC-$$i.gz created"; \
	done \

create-def-client-files:
	for i in {1..9}; do \
		mkfile 100m "collector/sample-file-resources/origin/def/layout01/DEF-$$i.gz"; \
		echo   "file collector/sample-file-resources/origin/def/layout01/DEF-$$i.gz created"; \
	done \

create-ghi-client-files:
	for i in {1..9}; do \
		mkfile 50k  "collector/sample-file-resources/origin/ghi/layout01/GHI-$$i.gz"; \
		echo   "file collector/sample-file-resources/origin/ghi/layout01/GHI-$$i.gz created"; \
	done \

clean-up:
	rm -rf collector/sample-file-resources/origin/abc/layout01/*
	rm -rf collector/sample-file-resources/origin/def/layout01/*
	rm -rf collector/sample-file-resources/origin/ghi/layout01/*
	rm -rf collector/sample-file-resources/destination/abc/layout01/*
	rm -rf collector/sample-file-resources/destination/def/layout01/*
	rm -rf collector/sample-file-resources/destination/ghi/layout01/*
