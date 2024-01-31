INSERT INTO client_route
(id, company, source_folder, target_folder, cloud_id, extension_type, processing_type, access_key_id, secret_access_key, active)
VALUES (1,'ABC','sample-file-resources/origin/abc/layout01/','sample-file-resources/destination/abc/layout01/', 1,'.gz','GROUP','foo', 'bar', true),
       (2,'DEF','sample-file-resources/origin/def/layout01/','sample-file-resources/destination/def/layout01/', 1,'.gz','INDIVIDUAL','foo', 'bar', true),
       (3,'GHI','sample-file-resources/origin/ghi/layout01/','sample-file-resources/destination/ghi/layout01/', 1,'.gz','GROUP','foo', 'bar', true),
       (4,'JKL','sample-file-resources/origin/jkl/layout01/','sample-file-resources/destination/jkl/layout01/', 1,'.gz','GROUP','foo', 'bar', false);
